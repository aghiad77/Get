package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Shops;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.ProductActivity;
import com.example.aghiad_pc.get.UI.ShopActivity;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.MyViewHolder> {

    private List<Shops> shopsList;
    private Context context;
    int row_index=0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        RelativeLayout relativelayout;


        MyViewHolder(View view) {
        super(view);
        view.setClickable(true);
        image = view.findViewById(R.id.shop_image);
        text = view.findViewById(R.id.logo_text);
        relativelayout = (RelativeLayout) itemView.findViewById(R.id.relative);
        Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
        text.setTypeface(face);
    }
}

    public ShopsAdapter(List<Shops> shopsList, Context context) {
        this.shopsList = shopsList;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shops_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Shops shop = shopsList.get(position);
        holder.text.setText(shop.getName());
        //holder.image.setBackgroundResource(shop.getLogo());
        new ImageDownloaderTask(holder.image).execute(shop.getLogo());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, shopsList.get(position).getName() , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ShopActivity.class);
                intent.putExtra("store", shopsList.get(position).getName());
                intent.putExtra("logo", shopsList.get(position).getLogo());
                context.startActivity(intent);
                /*if(row_index!=position)
                    CallGetCategories(shopsList.get(position).getName());
                row_index =position;
                notifyDataSetChanged();*/

            }
        });
        if(row_index ==position){
            holder.text.setTextColor(Color.parseColor("#131313"));
            //holder.image.setAlpha(1);
        }
        else
        {
            holder.text.setTextColor(Color.parseColor("#868585"));
            //holder.image.setAlpha((float) 0.5);
        }
    }

    private void CallGetCategories(String name) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_CATEGORIES);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_CATEGORIES, name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }
}
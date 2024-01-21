package com.example.aghiad_pc.get.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.ProductActivity;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    private List<Products> productsList;
    private  static Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, store, salary;
        ImageView image,add,favorite;


        MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            store = view.findViewById(R.id.product_store);
            name = view.findViewById(R.id.product_name);
            salary = view.findViewById(R.id.product_price);
            image= view.findViewById(R.id.product_image);
            add= view.findViewById(R.id.add);
            favorite= view.findViewById(R.id.favorite);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            store.setTypeface(face);
            name.setTypeface(face);
            salary.setTypeface(face);
        }
    }

    public ProductAdapter(List<Products> productsList, Context context) {
        this.productsList = productsList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_list, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Products product = productsList.get(position);
        //holder.title.setText(product.getComment());
        holder.name.setText(product.getName());
        holder.store.setText( "متجر "+product.getStore());
        holder.salary.setText("السعر "+product.getPrice()+"  ل.س");
        //holder.image.setBackgroundResource(product.getLogo());
        if(product.getIs_favorite().equals("0"))
            holder.favorite.setBackgroundResource(R.drawable.white_star);
        else
            holder.favorite.setBackgroundResource(R.drawable.yellow_star);
        new ImageDownloaderTask(holder.image).execute(product.getLogo());


        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", productsList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", productsList.get(position).getId());
                context.startActivity(intent);
            }
        });


        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(productsList.get(position).getIs_favorite().equals("0")){
                    CallCreateFavorite(productsList.get(position).getId(),"create");
                    holder.favorite.setBackgroundResource(R.drawable.yellow_star);
                    productsList.get(position).setIs_favorite("1");
                }else{
                    CallCreateFavorite(productsList.get(position).getId(),"drop");
                    holder.favorite.setBackgroundResource(R.drawable.white_star);
                    productsList.get(position).setIs_favorite("0");
                }
                // Call BrodcastReciver in service about white_star
            }
        });

    }

    @Override
    public int getItemCount() {
        return productsList.size();

    }

    private void CallCreateFavorite(String id,String status) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CREATE_FAVORITE);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_ID, id);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_STATUS, status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

}

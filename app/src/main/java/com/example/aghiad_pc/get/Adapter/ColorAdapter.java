package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.MyViewHolder> {


    private List<String> colorList;
    private Context context;
    int row_index=0;

    class MyViewHolder extends RecyclerView.ViewHolder {

        //ImageView image;
        TextView text;
        RelativeLayout relativelayout;
        MyViewHolder(View view) {
            super(view);
            //image = (ImageView) view.findViewById(R.id.color_circle);
            text = view.findViewById(R.id.category_text);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            text.setTypeface(face);
            relativelayout=(RelativeLayout) itemView.findViewById(R.id.relative);


        }
    }
    public ColorAdapter(List<String> colorList,Context context) {

        this.colorList = colorList;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String color = colorList.get(position);
        holder.text.setText(color);
        /*Drawable unwrappedDrawable = holder.image.getBackground();
        unwrappedDrawable.setColorFilter(android.graphics.Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
        holder.image.setBackground(unwrappedDrawable);*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(row_index!=position)
                    CallSelectedColor(colorList.get(position));
                row_index =position;
                notifyDataSetChanged();

            }
        });
        if(row_index ==position){
            holder.text.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.text.setTextColor(Color.parseColor("#868585"));
        }
    }

    private void CallSelectedColor(String color) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_COLOR_SELECTED);
        intent.putExtra(GetStrings.ACTION_BROADCAST_COLOR_SELECTED_MESSAGE, color);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }
}
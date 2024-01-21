package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class orderDetailsAdapter extends RecyclerView.Adapter<orderDetailsAdapter.MyViewHolder> {

    private List<orderDetails> orderList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderName, orderQuantity, orderSize, orderPrice,variant;
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            orderName = view.findViewById(R.id.name_value);
            orderQuantity = view.findViewById(R.id.quantity_value);
            orderSize = view.findViewById(R.id.size_value);
            orderPrice = view.findViewById(R.id.price_value);
            variant = view.findViewById(R.id.variant_value);
            image = view.findViewById(R.id.order_image);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            orderName.setTypeface(face);
            orderQuantity.setTypeface(face);
            orderSize.setTypeface(face);
            orderPrice.setTypeface(face);
            variant.setTypeface(face);

        }
    }

    public orderDetailsAdapter(List<orderDetails> orderList, Context context) {
        this.orderList = orderList;
        this.context=context;
    }

    @NonNull
    @Override
    public orderDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_details_list, parent, false);
        return new orderDetailsAdapter.MyViewHolder(itemView);
    }



    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull orderDetailsAdapter.MyViewHolder holder, int position) {

        orderDetails order = orderList.get(position);
        holder.orderName.setText(order.getProduct());
        holder.orderQuantity.setText(order.getCount());
        holder.variant.setText(order.getColor());
        if(order.getSize().equals("null"))
            holder.orderSize.setText("لا يوجد");
        else
            holder.orderSize.setText(order.getSize());
        holder.orderPrice.setText(order.getTotal_price() + " ل.س");
        /*Drawable unwrappedDrawable = holder.color.getBackground();
        unwrappedDrawable.setColorFilter(android.graphics.Color.parseColor(order.getColor()), PorterDuff.Mode.SRC_ATOP);
        holder.color.setBackground(unwrappedDrawable);*/
        new ImageDownloaderTask(holder.image).execute(order.getLogo());

    }

    @Override
    public int getItemCount() {
        return orderList.size();

    }
}
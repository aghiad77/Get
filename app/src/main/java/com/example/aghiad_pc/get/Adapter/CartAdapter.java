package com.example.aghiad_pc.get.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private List<orderDetails> CartList;
    private static Context sourceContext;
    //private static List<Integer> beforeDiscountList = new ArrayList<>() ;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, size,price,count,variant;   //,newpriceText,newpriceValue;
        ImageView image,delete;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name_value);
            price = view.findViewById(R.id.price_value);
            size = view.findViewById(R.id.size_value);
            //color = view.findViewById(R.id.color_value);
            variant = view.findViewById(R.id.variant_value);
            count = view.findViewById(R.id.quantity_value);
            //newpriceText = view.findViewById(R.id.product_new_price);
            //newpriceValue = view.findViewById(R.id.price_new_value);
            delete = view.findViewById(R.id.delete);
            image= view.findViewById(R.id.product_image);
            Typeface face = Typeface.createFromAsset(sourceContext.getAssets(), "Tajawal-Medium.ttf");
            price.setTypeface(face);
            name.setTypeface(face);
            size.setTypeface(face);
            count.setTypeface(face);
            variant.setTypeface(face);
            //newpriceText.setTypeface(face);
            //newpriceValue.setTypeface(face);

        }
    }

    public CartAdapter(List<orderDetails> productList, Context context) {
        this.CartList = productList;
        sourceContext=context;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_list, parent, false);
        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        orderDetails product = CartList.get(position);
        holder.name.setText(product.getProduct());
        holder.count.setText(product.getCount());
        holder.variant.setText(product.getColor());
        holder.price.setText(product.getPrice()+ " ل.س");
        //beforeDiscountList.add(Integer.valueOf(product.getPrice()));
        if(product.getSize().equals("null"))
            holder.size.setText("لا يوجد");
        else
            holder.size.setText(product.getSize());
        /*Drawable unwrappedDrawable = holder.color.getBackground();
        unwrappedDrawable.setColorFilter(android.graphics.Color.parseColor(product.getColor()), PorterDuff.Mode.SRC_ATOP);
        holder.color.setBackground(unwrappedDrawable);*/

        /*if(!(Integer.valueOf(product.getTotal_price())/Integer.valueOf(product.getPrice())==1) &&
                !(Integer.valueOf(product.getTotal_price())/Integer.valueOf(product.getPrice())==Integer.valueOf(product.getCount()))){
            holder.newpriceText.setVisibility(View.VISIBLE);
            holder.newpriceValue.setVisibility(View.VISIBLE);
            holder.newpriceValue.setText(product.getPrice()+ "  ل.س");
        }*/

        new ImageDownloaderTask(holder.image).execute(product.getLogo());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(!cn.getShortClassName().equals(".UI.FavoriteActivity"))
                CallDeleteProduct(CartList.get(position).getProduct_id()+";"+CartList.get(position).getColor()+";"+CartList.get(position).getSize()+";"+CartList.get(position).getCount());
                CartList.remove(CartList.get(position));
                notifyDataSetChanged();
            }
        });
    }

    private void CallDeleteProduct(String product) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_ADD_PRODUCT_TO_CART);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_ADD_PRODUCT_TO_CART, product);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_STATUS_ADD_PRODUCT_TO_CART, "delete");
        LocalBroadcastManager.getInstance(sourceContext).sendBroadcast(intent);
    }


    @Override
    public int getItemCount() {
        return CartList.size();

    }
}

package com.example.aghiad_pc.get.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghiad_pc.get.Model.Color;
import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.ProductActivity;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.MyViewHolder> {


    private List<Offer> offerList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName , price , store , endDate;
        ImageView image , details;

        MyViewHolder(View view) {
            super(view);
            productName= view.findViewById(R.id.product_name);
            price= view.findViewById(R.id.price);
            store= view.findViewById(R.id.store);
            endDate= view.findViewById(R.id.date);
            image= view.findViewById(R.id.product_image);
            details= view.findViewById(R.id.add);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            productName.setTypeface(face);
            price.setTypeface(face);
            store.setTypeface(face);
            endDate.setTypeface(face);
        }
    }

    public OfferAdapter(List<Offer> offerList , Context context) {
        this.offerList = offerList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offer_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Offer offer = offerList.get(position);
        holder.productName.setText(offer.getName());
        holder.price.setText(offer.getPrice() + " ل.س");
        holder.store.setText("متجر: "+offer.getStore());
        holder.endDate.setText("لغاية: "+offer.getEnd_at());
        new ImageDownloaderTask(holder.image).execute(offer.getLogo());
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", offerList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", offerList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offerList.size();

    }

}

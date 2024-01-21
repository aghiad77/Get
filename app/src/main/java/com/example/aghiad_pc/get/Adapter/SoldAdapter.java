package com.example.aghiad_pc.get.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.Model.soldProduct;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.ProductActivity;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoldAdapter extends RecyclerView.Adapter<SoldAdapter.MyViewHolder> {

    private List<soldProduct> soldList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView productName , oldPrice , newPrice , store , value;
        ImageView image , details;

        MyViewHolder(View view) {
            super(view);
            productName= view.findViewById(R.id.product_name);
            oldPrice= view.findViewById(R.id.old_price);
            newPrice= view.findViewById(R.id.new_price);
            store= view.findViewById(R.id.product_store);
            value= view.findViewById(R.id.value);
            image= view.findViewById(R.id.product_image);
            details= view.findViewById(R.id.add);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            productName.setTypeface(face);
            oldPrice.setTypeface(face);
            newPrice.setTypeface(face);
            store.setTypeface(face);
            value.setTypeface(face);
        }
    }

    public SoldAdapter(List<soldProduct> soldList , Context context) {
        this.soldList = soldList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sold_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SoldAdapter.MyViewHolder holder, int position) {
        soldProduct sold = soldList.get(position);
        holder.productName.setText(sold.getName());
        holder.oldPrice.setText(sold.getOld_price() + " ل.س");
        holder.newPrice.setText(sold.getNew_price() + " ل.س");
        holder.store.setText("متجر "+sold.getStore());
        if(!sold.getSold_amount_type().contains("%"))
            holder.value.setText(sold.getSold_amount_type()+ " ل.س");
        else
            holder.value.setText(sold.getSold_amount_type());
        new ImageDownloaderTask(holder.image).execute(sold.getLogo());
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", soldList.get(position).getId());
                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", soldList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return soldList.size();
    }
}

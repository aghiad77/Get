package com.example.aghiad_pc.get.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.subCategory;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class subCategoryAdapter extends RecyclerView.Adapter<subCategoryAdapter.MyViewHolder> {

    private List<subCategory> subcategoryList;
    private Context context;
    int row_index = 0;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            text = view.findViewById(R.id.subcategory_text);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            text.setTypeface(face);
        }
    }

    public subCategoryAdapter(List<subCategory> subcategoryList, Context context) {
        this.subcategoryList = subcategoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public subCategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subcategory_list, parent, false);
        return new subCategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(subCategoryAdapter.MyViewHolder holder, final int position) {
        subCategory subcategory = subcategoryList.get(position);
        holder.text.setText(subcategory.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (row_index != position)
                    CallGetProducts(subcategoryList.get(position).getName());
                row_index = position;
                notifyDataSetChanged();

            }
        });
        if (row_index == position) {
            holder.text.setTextColor(Color.parseColor("#000000"));
        } else {
            holder.text.setTextColor(Color.parseColor("#868585"));
        }
    }

    private void CallGetProducts(String name) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_BY_SUBGATEGORIES_FOR_STORES);
        if(name.equals("الكل"))
            name="all";
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES_BY_SUBCATEGORY, name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return subcategoryList.size();
    }
}
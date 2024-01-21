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
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private String target;
    int row_index=0;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            text = view.findViewById(R.id.category_text);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            text.setTypeface(face);
        }
    }
    public CategoryAdapter(List<Category> categoryList,Context context, String target) {
        this.categoryList = categoryList;
        this.context=context;
        this.target = target;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Category category = categoryList.get(position);
        holder.text.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(row_index!=position)
                    CallGetProducts(categoryList.get(position).getName());
                if(position == 0 && target.equals("home"))
                    CallGetProducts(categoryList.get(position).getName());
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

    private void CallGetProducts(String name) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_FOR_STORES);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES, name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public int getItemPosition(String eventId) {
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getName().equals(eventId)) {
                row_index = i;
                return i;
            }
        }
        return 0;
    }
}
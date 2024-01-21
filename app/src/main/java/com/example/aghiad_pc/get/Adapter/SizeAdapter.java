package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Size;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {
    private List<String> sizeList;
    private Context context;
    int row_index=0;

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        RelativeLayout relativelayout;
        MyViewHolder(View view) {
            super(view);
            text = view.findViewById(R.id.size_text);
            relativelayout=(RelativeLayout) itemView.findViewById(R.id.relative);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            text.setTypeface(face);


        }
    }
    public SizeAdapter(List<String> sizeList,Context context)
    {
        this.sizeList = sizeList;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.size_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String size = sizeList.get(position);
        holder.text.setText(size);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(row_index!=position)
                    CallSelectedSize(sizeList.get(position));
                row_index =position;
                notifyDataSetChanged();

            }
        });
        if(row_index ==position){
            holder.text.setTextColor(Color.parseColor("#000000"));
        }
        else
        {
            holder.text.setTextColor(Color.parseColor("#868585"));
        }
    }

    private void CallSelectedSize(String name) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SIZE_SELECTED);
        intent.putExtra(GetStrings.ACTION_BROADCAST_SIZE_SELECTED_MESSAGE, name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }
}
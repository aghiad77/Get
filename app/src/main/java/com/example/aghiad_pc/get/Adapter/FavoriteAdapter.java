package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Favorite;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.ProductActivity;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder> {

    private List<Favorite>  favoriteList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, cost,delete,details;
        ImageView image;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.favorite_title);
            cost = view.findViewById(R.id.favorite_cost);
            delete = view.findViewById(R.id.favorite_delete);
            details = view.findViewById(R.id.favorite_details);
            image=(ImageView) view.findViewById(R.id.favorite_image);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            title.setTypeface(face);
            cost.setTypeface(face);
            details.setTypeface(face);
            delete.setTypeface(face);
        }
    }

    public FavoriteAdapter(List<Favorite> favoriteList, Context context) {
        this.favoriteList = favoriteList;
        this.context=context;
    }

    @NonNull
    @Override
    public FavoriteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_list, parent, false);
        return new FavoriteAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Favorite favorite=favoriteList.get(position);
        holder.title.setText(favorite.getName());
        holder.cost.setText(favorite.getPrice()+ "  ل.س");
        new ImageDownloaderTask(holder.image).execute(favorite.getLogo());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refreshAdapter(favoriteList.get(position).getProduct_id());
                CallDeletefavorite(favoriteList.get(position).getProduct_id(),"drop");
                favoriteList.remove(favoriteList.get(position));
                notifyDataSetChanged();
            }
        });

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ID", favoriteList.get(position).getProduct_id());
                context.startActivity(intent);
            }
        });
    }

    private void refreshAdapter(String id) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_DELETE_FAVORITE);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_FAVORITE, id);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void CallDeletefavorite(String id,String status) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CREATE_FAVORITE);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_ID, id);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_STATUS, status);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }
    @Override
    public int getItemCount() {
        return favoriteList.size();

    }
}

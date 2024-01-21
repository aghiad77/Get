package com.example.aghiad_pc.get.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Notification;
import com.example.aghiad_pc.get.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private List<Notification> notificationList;
    private Context context;
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,text;
        ImageView delete;
        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.notification_title);
            text = view.findViewById(R.id.notification_text);
            delete = view.findViewById(R.id.notification_menu);

            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            title.setTypeface(face);
            text.setTypeface(face);



        }
    }
    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context=context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Notification notification = notificationList.get(position);
        holder.title.setText(notification.getTitle());
        holder.text.setText(notification.getText());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.delete);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                deletenotification(position);
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.show();
            }
        });

    }

    private void deletenotification(int position) {
        Database db=new Database(context);
        db.deleteNotification(notificationList.get(position).getId());
        db.close();
        notificationList.remove(notificationList.get(position));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
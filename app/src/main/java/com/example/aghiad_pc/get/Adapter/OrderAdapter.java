package com.example.aghiad_pc.get.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.UI.MyOrdersActivity;
import com.example.aghiad_pc.get.UI.OrderDetailsActivity;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private List<Orders>  orderList;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderID, orderDate, orderShipped, totalPrice, transportCost,status,transportMsg;
        Button more;
        ImageView delete;

        MyViewHolder(View view) {
            super(view);
            orderID = view.findViewById(R.id.order_id);
            orderDate = view.findViewById(R.id.order_date);
            orderShipped = view.findViewById(R.id.order_shipped_date);
            totalPrice = view.findViewById(R.id.order_total_price);
            transportCost = view.findViewById(R.id.order_transport_price);
            status = view.findViewById(R.id.order_status);
            transportMsg = view.findViewById(R.id.order_transport_message);
            delete = view.findViewById(R.id.delete);
            more = view.findViewById(R.id.more);
            Typeface face = Typeface.createFromAsset(context.getAssets(), "Tajawal-Medium.ttf");
            orderID.setTypeface(face);
            orderDate.setTypeface(face);
            orderShipped.setTypeface(face);
            totalPrice.setTypeface(face);
            transportCost.setTypeface(face);
            status.setTypeface(face);
            transportMsg.setTypeface(face);
        }
    }

    public OrderAdapter(List<Orders> orderList, Context context) {
        this.orderList = orderList;
        this.context=context;
    }

    @NonNull
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);
        return new OrderAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Orders order = orderList.get(position);
        holder.orderID.setText("رقم الطلبية:  "+ order.getId());
        holder.orderDate.setText("تاريخ الطلبية:  "+ order.getOrdered_at());
        holder.totalPrice.setText("المبلغ الكلي + أجور التوصيل:  "+ order.getSum_total_price() + "  ل.س");
        holder.status.setText("حالة الطلبية:  "+ order.getStatus());
        holder.transportCost.setText("أجرة التوصيل:  "+ order.getTransport_cost()+ " ل.س");
        holder.transportMsg.setText(order.getTransport_message());
        if(order.getShipped_at().equals("null"))
            holder.orderShipped.setText("تاريخ الشحن:  غير محدد");
        else
            holder.orderShipped.setText("تاريخ الشحن:  "+ order.getShipped_at());

        if(order.getStatus().equals("في الانتظار")){
            holder.delete.setVisibility(View.VISIBLE);
        }else if(order.getStatus().equals("قيد المعالجة")){
            holder.delete.setVisibility(View.INVISIBLE);
        }else if(order.getStatus().equals("قيد التسليم")){
            holder.delete.setVisibility(View.INVISIBLE);
        }else if(order.getStatus().equals("مكتمل")){
            holder.delete.setVisibility(View.INVISIBLE);
        } else if(order.getStatus().equals("ملغى")) {
            holder.delete.setVisibility(View.INVISIBLE);
        }

        /*if(order.getShipmethod().equals("null"))
            holder.transportCost.setText("أجور النقل:  غير محددة");
        else if(order.getShipmethod().equals("شحن")){
            holder.transportCost.setText(order.getTransport_cost());
        }else if(order.getShipmethod().equals("توصيل")){
            holder.transportCost.setText("أجرة التوصيل:  "+ order.getTransport_cost());
        }else if(order.getShipmethod().equals("شحن وتوصيل")) {
            holder.transportCost.setText("أجرة الشحن والتوصيل:  " + order.getTransport_cost());
        }*/

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallDeleteOrder(orderList.get(position).getId());
            }
        });

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallOrderDetails(orderList.get(position).getId());
            }
        });
    }

    private void CallDeleteOrder(String id) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_DELETE_ORDERS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS, id);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS_STATUS, "callDelete");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
    private void CallOrderDetails(String id){
        Intent intent_ = new Intent(context, OrderDetailsActivity.class);
        intent_.putExtra("orderID", id);
        context.startActivity(intent_);
        //Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CALL_ORDER_DETAILS);
        //intent.putExtra(GetStrings.MESSAGE_BROADCAST_CALL_ORDER_DETAILS, id);
        //LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return orderList.size();

    }
}

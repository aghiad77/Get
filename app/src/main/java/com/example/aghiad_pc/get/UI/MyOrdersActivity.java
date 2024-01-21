package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghiad_pc.get.Adapter.OrderAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Order;
import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyOrdersActivity extends AppCompatActivity {


    private BroadcastReceiver showOrders = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String orders = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ORDERS);
            if(orders!=null && !orders.equals("")) {
                prepareOrderssData(getOrdersFromString(orders));
            }else{
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver deleteOrder = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String id = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS);
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS_STATUS);
            if(status.equals("callDelete"))
                deleteOrder(id);
            else if (status.equals("success")) {
                refreshOrders(id);
                Toast.makeText(getApplication(),"تم إلغاء الطلبية بنجاح",Toast.LENGTH_LONG).show();
            }

        }
    };

    private BroadcastReceiver callOrderDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String id = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CALL_ORDER_DETAILS);
            if(id!=null && !id.equals("")){
                   //Intent intent_ = new Intent(MyOrdersActivity.this, OrderDetailsActivity.class);
                   //intent_.putExtra("orderID", id);
                   //startActivity(intent_);
                   //finish();
            }
        }
    };

    private List<Orders> orderList = new ArrayList<>();
    private OrderAdapter orderAdapter;
    private TextView title,emptyText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        initialOrders();

        title = (TextView) findViewById(R.id.orders_titile);
        emptyText = (TextView) findViewById(R.id.hintText);
        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        emptyText.setTypeface(face);

    }

    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(MyOrdersActivity.this).unregisterReceiver(showOrders);
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).unregisterReceiver(deleteOrder);
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).unregisterReceiver(callOrderDetails);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).registerReceiver(showOrders, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_ORDERS));
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).registerReceiver(deleteOrder, new IntentFilter(GetStrings.ACTION_BROADCAST_DELETE_ORDERS));
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).registerReceiver(callOrderDetails, new IntentFilter(GetStrings.ACTION_BROADCAST_CALL_ORDER_DETAILS));
        CallServiceReceiver();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void CallServiceReceiver() {

        // Call GET Orders
        if(orderList!=null && orderList.isEmpty()) {
            Intent intent1 = new Intent(GetStrings.ACTION_BROADCAST_GET_ORDERS);
            LocalBroadcastManager.getInstance(MyOrdersActivity.this).sendBroadcast(intent1);
        }

    }
    @SuppressLint("WrongConstant")
    private void initialOrders() {
        RecyclerView recyclerViewProduct = findViewById(R.id.recyclerView_orders);
        recyclerViewProduct.hasFixedSize();
        orderAdapter = new OrderAdapter(orderList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(orderAdapter);
        //prepareProductsData();
    }
    private void prepareOrderssData(List<Orders> orders) {
        for(Orders order:orders)
            orderList.add(order);
        orderAdapter.notifyDataSetChanged();
    }

    private void deleteOrder(String id) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CANCEL_ORDER);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CANCEL_ORDER, id);
        LocalBroadcastManager.getInstance(MyOrdersActivity.this).sendBroadcast(intent);
    }

    private void refreshOrders(String id) {
        for(Orders order:orderList){
            if (order.getId().equals(id)){
                order.setStatus("ملغى");
                orderAdapter.notifyDataSetChanged();
            }
        }
    }

    public List<Orders> getOrdersFromString(String result){

        List<Orders> orders=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Orders pro=new Orders(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4],oneObject[5],oneObject[6],oneObject[7],oneObject[8]);
                orders.add(pro);
            }

        }
        return orders;
    }


}

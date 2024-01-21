package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.aghiad_pc.get.Adapter.orderDetailsAdapter;
import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private BroadcastReceiver showOrderDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String orders = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ORDER_DETAILS);
            if(orders!=null && !orders.isEmpty()) {
                prepareOrderssData(getOrdersFromString(orders));
            }
        }
    };



    private List<orderDetails> orderList = new ArrayList<>();
    private orderDetailsAdapter orderAdapter;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initialOrders();
        getData();

        title = (TextView) findViewById(R.id.orders_titile);
        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);

    }

    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(OrderDetailsActivity.this).unregisterReceiver(showOrderDetails);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(OrderDetailsActivity.this).registerReceiver(showOrderDetails, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_ORDER_DETAILS));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void getData() {
        try {
            CallServiceReceiver(getIntent().getStringExtra("orderID"));
        }catch (Exception e){
            return;
        }
    }
    private void CallServiceReceiver(String id) {

        // Call GET Order Details
        if(orderList!=null && orderList.isEmpty()) {
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_ORDER_DETAILS);
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_ORDER_DETAILS_ID, id);
            LocalBroadcastManager.getInstance(OrderDetailsActivity.this).sendBroadcast(intent);
        }

    }

    @SuppressLint("WrongConstant")
    private void initialOrders() {
        RecyclerView recyclerViewProduct = findViewById(R.id.recyclerView_orders);
        recyclerViewProduct.hasFixedSize();
        orderAdapter = new orderDetailsAdapter(orderList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(orderAdapter);
        //prepareProductsData();
    }
    private void prepareOrderssData(List<orderDetails> orders) {
        for(orderDetails order:orders)
            orderList.add(order);
        orderAdapter.notifyDataSetChanged();
    }

    public List<orderDetails> getOrdersFromString(String result){

        List<orderDetails> orders=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                orderDetails pro=new orderDetails(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4],oneObject[5],oneObject[6],oneObject[7]);
                orders.add(pro);
            }

        }
        return orders;
    }
}

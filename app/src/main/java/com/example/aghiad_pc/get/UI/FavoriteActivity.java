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
import com.example.aghiad_pc.get.Adapter.FavoriteAdapter;
import com.example.aghiad_pc.get.Model.Favorite;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteActivity extends AppCompatActivity {

    private List<Favorite> favoriteList = new ArrayList<>();
    private FavoriteAdapter favoriteAdapter;
    private TextView title,emptyText;
    private String fragment;

    private BroadcastReceiver showFavorites = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String favorites = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_FAVORITE);
            if(favorites!=null && !favorites.equals(""))
                 prepareProductsData(getFavoritesFromString(favorites));
            else
                emptyText.setVisibility(View.VISIBLE);
        }
    };

    private BroadcastReceiver deleteFavorite = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String id = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_DELETE_FAVORITE);
            refreshOrders(id);
            if(favoriteList.isEmpty()){
                //favoriteList.clear();
                //favoriteAdapter.notifyDataSetChanged();
                emptyText.setVisibility(View.VISIBLE);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        //getFragment();
        initialFavorites();

        title = (TextView) findViewById(R.id.orders_titile);
        emptyText = (TextView) findViewById(R.id.hintText);
        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        emptyText.setTypeface(face);
    }

    private void getFragment() {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fragment = extras.getString("fragment");
        } else {
            // handle case
        }
    }


    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(FavoriteActivity.this).unregisterReceiver(showFavorites);
        LocalBroadcastManager.getInstance(FavoriteActivity.this).unregisterReceiver(deleteFavorite);
        super.onDestroy();
    }

    @Override
    public void onPause() {

        LocalBroadcastManager.getInstance(FavoriteActivity.this).unregisterReceiver(showFavorites);
        LocalBroadcastManager.getInstance(FavoriteActivity.this).unregisterReceiver(deleteFavorite);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(FavoriteActivity.this).registerReceiver(showFavorites, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_FAVORITE));
        LocalBroadcastManager.getInstance(FavoriteActivity.this).registerReceiver(deleteFavorite, new IntentFilter(GetStrings.ACTION_BROADCAST_DELETE_FAVORITE));
        CallServiceReceiver();
    }


    private void CallServiceReceiver() {

        // Call GET Orders
        if(favoriteList!=null && favoriteList.isEmpty()) {
            Intent intent1 = new Intent(GetStrings.ACTION_BROADCAST_GET_FAVORITE);
            LocalBroadcastManager.getInstance(FavoriteActivity.this).sendBroadcast(intent1);
        }

    }

    @SuppressLint("WrongConstant")
    private void initialFavorites() {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewProduct = findViewById(R.id.recyclerView_favorite);
        recyclerViewProduct.hasFixedSize();
        favoriteAdapter = new FavoriteAdapter(favoriteList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(favoriteAdapter);
    }
    private void prepareProductsData(List<Favorite> favorites) {
        for(Favorite favorite : favorites)
            favoriteList.add(favorite);
        favoriteAdapter.notifyDataSetChanged();
    }
    private void refreshOrders(String id) {
        List<Favorite> favorites=new ArrayList<>();
        favorites=favoriteList;
        for(Favorite favorite:favorites){
            if (favorite.getProduct_id().equals(id)){
                favorites.remove(favorite);
            }
        }
        favoriteList.clear();
        favoriteList=favorites;
        favoriteAdapter.notifyDataSetChanged();
    }
    public List<Favorite> getFavoritesFromString(String result){

        List<Favorite> favorites=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Favorite fav=new Favorite(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4]);
                favorites.add(fav);
            }

        }
        return favorites;
    }
}

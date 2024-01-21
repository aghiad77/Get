package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aghiad_pc.get.Adapter.NotificationAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Notification;
import com.example.aghiad_pc.get.Model.couponProduct;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationFragment extends Fragment {


    private BroadcastReceiver getNotifications = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String notifications = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_NOTIFICATIONS);
            if(!notifications.equals("") && notifications!=null)
                prepareNotificationsData(getNotificationsFromString(notifications));
            else
                prepareNotificationsData(db.getNotification());
        }
    };

    private DrawerLayout mDrawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView cartNumber,title, emptyText;
    private Database db;
    private ImageView cart;

    private List<Notification> notificationList = new ArrayList<>();
    private NotificationAdapter notificationAdapter;



    public static NotificationFragment newInstance() {
        NotificationFragment fragment = new NotificationFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        initial(view);
        initialNotifications(view);

        GetUtilities.startgetService(getContext());
        RelativeLayout slideMenu=(RelativeLayout) view.findViewById(R.id.slide);
        slideMenu.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.END);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CartFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                navigation.getMenu().findItem(R.id.navigation_cart).setChecked(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(getNotifications, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_NOTIFICATIONS));
        CallgetNotifications();
    }

    private void CallgetNotifications() {
        if(notificationList!=null && notificationList.isEmpty()) {
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_NOTIFICATIONS);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }
    }

    private void initial(View view) {
        db=new Database(getContext());
        cartNumber = (TextView) view.findViewById(R.id.cart_count);
        setCartNumber();
        title = (TextView) view.findViewById(R.id.cart_titile);
        cart = (ImageView) view.findViewById(R.id.cart_image);
        emptyText = (TextView) view.findViewById(R.id.hintText);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        emptyText.setTypeface(face);
        navigationView=(NavigationView) view.findViewById(R.id.navigation_drawer);
        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, R.string.drawer_open, R.string.drawer_close);
        Menu nav_Menu = navigationView.getMenu();
        if(db.gettype().equals("2"))
            nav_Menu.findItem(R.id.nav_myShop).setVisible(true);
        else
            nav_Menu.findItem(R.id.nav_myShop).setVisible(false);
        // Where do I put this?
        mDrawerToggle.syncState();

        setupDrawerContent(navigationView);

        for (int i=0;i<nav_Menu.size();i++) {
            MenuItem mi = nav_Menu.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    GetUtilities.applyFontToMenuItem(subMenuItem,getContext());
                }
            }

            //the method we have create in activity
            GetUtilities.applyFontToMenuItem(mi,getContext());
        }
    }

    private void setCartNumber() {
        if(getService.cartCounter>0)
            cartNumber.setText(Integer.toString(getService.cartCounter));
        else
            cartNumber.setText(" ");
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        //Checking if the item is in checked state or not, if not make it in checked state
                        if (menuItem.isChecked()) menuItem.setChecked(false);
                        else menuItem.setChecked(true);

                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }
    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position

        Intent intent;
        switch (menuItem.getItemId()) {

            case R.id.nav_my_orders:
                intent = new Intent(getActivity(), MyOrdersActivity.class);
                startActivity(intent);
                //getActivity().finish();
                break;

            case R.id.nav_favorite:
                intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
                //getActivity().finish();
                break;

            case R.id.nav_account:
                intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_complaints:
                intent = new Intent(getActivity(), ComplaintsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_condition:
                intent = new Intent(getActivity(), ConditionActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_contact:
                intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_myShop:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://getonline-sy.com"));
                startActivity(browserIntent);
                break;

            case R.id.nav_logout:
                Database db = new Database(getContext());
                db.onUpgrade(db.getWritableDatabase(),0,1);
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

        mDrawer.closeDrawers();
    }

    @SuppressLint("WrongConstant")
    private void initialNotifications(View view) {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewShops = view.findViewById(R.id.recyclerView_notification);
        notificationAdapter = new NotificationAdapter(notificationList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(notificationAdapter);
    }
    private void prepareNotificationsData(List<Notification> list) {
        if(list!=null) {
            for (Notification noti : list)
                notificationList.add(noti);
            notificationAdapter.notifyDataSetChanged();
        }
        if(notificationList.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
    }

    public List<Notification> getNotificationsFromString(String result){

        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                db.addNotification(oneObject[0],oneObject[1],oneObject[2]);
            }

        }
        return db.getNotification();
    }
}

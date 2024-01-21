package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghiad_pc.get.Adapter.CategoryAdapter;
import com.example.aghiad_pc.get.Adapter.ProductAdapter;
import com.example.aghiad_pc.get.Adapter.ShopsAdapter;
import com.example.aghiad_pc.get.Adapter.subCategoryAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.Model.Shops;
import com.example.aghiad_pc.get.Model.subCategory;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ShopFragment extends Fragment {



    private BroadcastReceiver showStores = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String stores = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_HOME_FRAGMENT);
            if(!stores.equals("") && stores!=null) {
                prepareShopsData(getShopsFromString(stores));
            }
        }
    };


    private DrawerLayout mDrawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerViewShops;

    private List<Shops> shopsList = new ArrayList<>();
    private ShopsAdapter shopAdapter;

    private TextView cartNumber, title;
    private EditText searchText;
    private ImageView search , cart;

    public static ShopFragment newInstance() {
        ShopFragment fragment = new ShopFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shops, container, false);
        initial(view);
        initialShops(view);


        RelativeLayout slideMenu=(RelativeLayout) view.findViewById(R.id.slide);

        slideMenu.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.END);
            }
        });


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                //getStoresSearch(s.toString());
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dismissKeyboard(getActivity());
                    getStoresSearch(searchText.getText().toString());
                    return true;
                }
                return false;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    dismissKeyboard(getActivity());
                    getStoresSearch(searchText.getText().toString());
                } catch (Exception e) {

                }
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

    private void initial(View view) {
        cartNumber = (TextView) view.findViewById(R.id.cart_count);
        setCartNumber();
        title = (TextView) view.findViewById(R.id.cart_titile);
        searchText = (EditText) view.findViewById(R.id.edittext);
        search = (ImageView) view.findViewById(R.id.image_search);
        cart = (ImageView) view.findViewById(R.id.cart_image);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        navigationView=(NavigationView) view.findViewById(R.id.navigation_drawer);
        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, R.string.drawer_open, R.string.drawer_close);
        Database db = new Database(getContext());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showStores, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES_HOME_FRAGMENT));
        GetUtilities.startgetService(getContext());
        CallServiceReceiver();
    }

    private void CallServiceReceiver() {

        // Call GET STORES
        if(shopsList!=null && shopsList.isEmpty()) {
            Intent intent1 = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES);
            intent1.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SEARCH, " ");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent1);
        }

    }

    private void getStoresSearch(String text) {

        if(shopsList!=null && !shopsList.isEmpty()){
            shopsList.clear();
            shopAdapter.notifyDataSetChanged();
        }
        // Call GET STORES
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SEARCH, text);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showStores);
        super.onPause();
    }

    private void setCartNumber() {
        if(getService.cartCounter>0)
            cartNumber.setText(Integer.toString(getService.cartCounter));
        else
            cartNumber.setText(" ");
    }

    private void initialShops(View view) {
        // Add the following lines to create RecyclerView
        recyclerViewShops = view.findViewById(R.id.recyclerView_shops);
        shopAdapter = new ShopsAdapter(shopsList,getContext());
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),3);
        //mLayoutManager.setReverseLayout(true);
        //mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(shopAdapter);
    }
    private void prepareShopsData(List<Shops> shops) {
        for(Shops shop : shops)
            shopsList.add(shop);
        shopAdapter.notifyDataSetChanged();
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

    public List<Shops> getShopsFromString(String result){

        List<Shops> shops=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Shops sho=new Shops(oneObject[0],oneObject[1],oneObject[2],oneObject[3]);
                shops.add(sho);
            }

        }
        return shops;
    }
    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}

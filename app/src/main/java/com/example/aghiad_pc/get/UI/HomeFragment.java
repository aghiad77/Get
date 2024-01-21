package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ActionTypes;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.interfaces.TouchListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.aghiad_pc.get.Adapter.CategoryAdapter;
import com.example.aghiad_pc.get.Adapter.OfferAdapter;
import com.example.aghiad_pc.get.Adapter.ProductAdapter;
import com.example.aghiad_pc.get.Adapter.ShopsAdapter;
import com.example.aghiad_pc.get.Adapter.SoldAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Ads;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.Model.Shops;
import com.example.aghiad_pc.get.Model.soldProduct;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

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

public class HomeFragment extends Fragment {

    private DrawerLayout mDrawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerViewShops;
    private TextView cartNumber , shopText , productText,soldText,categoryText, offerText;
    private Button shopall ,categoryall; //, productall,offerall
    private EditText searchText;
    private ImageView search , cart , line_3 , line_4;
    private ImageSlider slider;
    private List<SlideModel> slideModels=new ArrayList<>();
    private List<Ads> Ads=new ArrayList<>();
    Database db;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    private List<Shops> shopsList = new ArrayList<>();
    private ShopsAdapter shopAdapter;

    private List<Products> productsList = new ArrayList<>();
    private ProductAdapter productAdapter;

    private List<soldProduct> soldList = new ArrayList<>();
    private SoldAdapter soldAdapter;

    private List<Offer> offerList = new ArrayList<>();
    private OfferAdapter offerAdapter;


    private BroadcastReceiver showProducts = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String products = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_HOME_FRAGMENT);
            if(!products.equals("") && products!=null)
                prepareProductsData(getProductsFromString(products));
        }
    };

    private BroadcastReceiver showSoldProducts = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String soldproducts = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT);
            if(!soldproducts.equals("") && soldproducts!=null)
                prepareSoldProductsData(getSoldProductsFromString(soldproducts));
        }
    };

    private BroadcastReceiver showCategories = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String categories = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_CATEGORIES_HOME_FRAGMENT);
            System.out.println(categories);
            if(!categories.equals("") && categories!=null)
                prepareCategoriesData(getCategoriesFromString(categories));
        }
    };

    private BroadcastReceiver showStores = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String stores = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_HOME_FRAGMENT);
            if(!stores.equals("") && stores!=null)
                prepareShopsData(getShopsFromString(stores));
        }
    };

    private BroadcastReceiver showOffer = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String productsOffer = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT);
            if(!productsOffer.equals("") && productsOffer!=null){
                prepareOfferProductsData(getOfferProductsFromString(productsOffer));
            }
        }
    };

    private BroadcastReceiver showImages = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_LOAD_IMAGES_HOME_FRAGMENT);
            if(status.equals("stores")) {
                shopAdapter.notifyDataSetChanged();
            } else {
                productAdapter.notifyDataSetChanged();
            }
        }
    };

    private BroadcastReceiver showAds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ADS);
            if(status!=null && !status.equals(""))
                fullImageSlider(getAdsFromString(status));

        }
    };

    private BroadcastReceiver moveToCtegories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String name = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES);
            Bundle bundle = new Bundle();
            bundle.putString("name",name); // Put anything what you want
            bundle.putString("target","home");
            Fragment fragment = new OfferFragment();
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
            navigation.getMenu().findItem(R.id.navigation_categories).setChecked(true);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    };

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initial(view);
        initialShops(view);
        initialProducts(view);
        initialSoldProducts(view);
        initialCategories(view);
        initialOfferProducts(view);

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
                //getProductsSearch(s.toString());
                //getSoldsSearch(s.toString());
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dismissKeyboard(getActivity());
                    getStoresSearch(searchText.getText().toString());
                    getProductsSearch(searchText.getText().toString());
                    getSoldsSearch(searchText.getText().toString());
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
                    getProductsSearch(searchText.getText().toString());
                    getSoldsSearch(searchText.getText().toString());
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

        /*shopall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ShopFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                navigation.getMenu().findItem(R.id.navigation_shops).setChecked(true);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });*/

        categoryall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",""); // Put anything what you want
                Fragment fragment = new OfferFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                navigation.getMenu().findItem(R.id.navigation_categories).setChecked(true);
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showSoldProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_GATEGORIES_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showStores, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showImages, new IntentFilter(GetStrings.ACTION_BROADCAST_LOAD_IMAGES_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showAds, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_ADS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(moveToCtegories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_FOR_STORES));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showOffer, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT));
        GetUtilities.startgetService(getContext());
        CallServiceReceiver();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showProducts);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showSoldProducts);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showStores);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showImages);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showAds);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showCategories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(moveToCtegories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showOffer);
        super.onPause();
    }

    public void initial(View view){

        db=new Database(getContext());
        cartNumber = (TextView) view.findViewById(R.id.cart_count);
        shopText = (TextView) view.findViewById(R.id.shops_text);
        productText = (TextView) view.findViewById(R.id.products_text);
        categoryText = (TextView) view.findViewById(R.id.categories_text);
        soldText = (TextView) view.findViewById(R.id.solds_text);
        offerText = (TextView) view.findViewById(R.id.offers_text);
        //shopall = (Button) view.findViewById(R.id.shops_all);
        //productall = (Button) view.findViewById(R.id.products_all);
        categoryall = (Button) view.findViewById(R.id.categories_all);
        //offerall = (Button) view.findViewById(R.id.offers_all);
        searchText = (EditText) view.findViewById(R.id.edittext);
        search = (ImageView) view.findViewById(R.id.image_search);
        cart = (ImageView) view.findViewById(R.id.cart_image);
        line_3 = (ImageView) view.findViewById(R.id.line_3);
        line_4 = (ImageView) view.findViewById(R.id.line_4);
        slider = (ImageSlider) view.findViewById(R.id.image_slider);
        setCartNumber();
        navigationView=(NavigationView) view.findViewById(R.id.navigation_drawer);
        mDrawer = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        Menu nav_Menu = navigationView.getMenu();
        if(db.gettype().equals("2"))
            nav_Menu.findItem(R.id.nav_myShop).setVisible(true);
        else
            nav_Menu.findItem(R.id.nav_myShop).setVisible(false);
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawer, R.string.drawer_open, R.string.drawer_close);
        // Where do I put this?
        mDrawerToggle.syncState();

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Tajawal-Medium.ttf");
        shopText.setTypeface(face);
        productText.setTypeface(face);
        categoryText.setTypeface(face);
        soldText.setTypeface(face);
        soldText.setVisibility(View.INVISIBLE);
        offerText.setTypeface(face);
        offerText.setVisibility(View.INVISIBLE);
        line_3.setVisibility(View.INVISIBLE);
        line_4.setVisibility(View.INVISIBLE);
        //shopall.setTypeface(face);
        //productall.setTypeface(face);
        categoryall.setTypeface(face);
        //offerall.setTypeface(face);
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

    private void CallServiceReceiver() {

        // Call GET GATEGORIES
        if(categoryList!=null && categoryList.isEmpty()) {
            Intent intent2 = new Intent(GetStrings.ACTION_BROADCAST_GET_ALL_CATEGORIES);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent2);
        }

        // Call GET STORES
        if(shopsList!=null && shopsList.isEmpty()) {
            Intent intent1 = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES);
            intent1.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SEARCH, " ");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent1);
        }


        // Call GET PRODUCTS
        if(productsList!=null && productsList.isEmpty()) {
            Intent intent3 = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS);
            intent3.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SEARCH, " ");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent3);
        }

        // Call GET OFFER PRODUCTS
        if(offerList!=null && offerList.isEmpty()) {
            Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_Offer);
            intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE, "");
            intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE_SEARCH, "");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent_);
        }

        // Call GET SOLD PRODUCTS
        if(soldList!=null && soldList.isEmpty()) {
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS);
            intent.putExtra(GetStrings. MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_STORE, " ");
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_SEARCH, " ");
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }

        // Call GET Ads
        if(slideModels!=null && slideModels.isEmpty()) {
            Intent intent2 = new Intent(GetStrings.ACTION_BROADCAST_GET_ADS);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent2);
        }

    }
    private void getProductsSearch(String text) {

        if(productsList!=null && !productsList.isEmpty()){
            productsList.clear();
            productAdapter.notifyDataSetChanged();
        }
        // Call GET PRODUCTS
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SEARCH, text);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
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

    private void getSoldsSearch(String text) {

        if(soldList!=null && !soldList.isEmpty()){
            soldList.clear();
            soldAdapter.notifyDataSetChanged();
        }
        // Call GET SOLDS
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_SEARCH, text);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawer.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawer.closeDrawer(Gravity.RIGHT);
                } else {
                    mDrawer.openDrawer(Gravity.RIGHT);
                }
                return true;
            //case R.id.:
            //  return true;
        }
        return super.onOptionsItemSelected(item);
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
                ///intent.putExtra("fragment", "HomeFragment");
                startActivity(intent);
                // getActivity().finish();
                break;

            case R.id.nav_favorite:
                intent = new Intent(getActivity(), FavoriteActivity.class);
                //intent.putExtra("fragment", "HomeFragment");
                startActivity(intent);
                // getActivity().finish();
                break;

            case R.id.nav_account:
                intent = new Intent(getActivity(), AccountActivity.class);
                //intent.putExtra("fragment", "HomeFragment");
                startActivity(intent);
                break;

            case R.id.nav_complaints:
                intent = new Intent(getActivity(), ComplaintsActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_condition:
                intent = new Intent(getActivity(), ConditionActivity.class);
                //intent.putExtra("fragment", "HomeFragment");
                startActivity(intent);
                //getActivity().finish();
                break;

            case R.id.nav_contact:
                intent = new Intent(getActivity(), ContactActivity.class);
                //intent.putExtra("fragment", "HomeFragment");
                startActivity(intent);
                break;

            case R.id.nav_myShop:
                //intent = new Intent(getActivity(), MyShopActivity.class);
                //intent.putExtra("fragment", "HomeFragment");
                //startActivity(intent);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://getonline-sy.com"));
                startActivity(browserIntent);
                break;

            case R.id.nav_logout:
                db.onUpgrade(db.getWritableDatabase(),0,1);
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

        mDrawer.closeDrawers();
    }
    private void setCartNumber() {
        if(getService.cartCounter>0)
            cartNumber.setText(Integer.toString(getService.cartCounter));
        else
            cartNumber.setText(" ");
    }


    private void initialShops
            (View view) {
        // Add the following lines to create RecyclerView
        recyclerViewShops = view.findViewById(R.id.recyclerView_shops);
        shopAdapter = new ShopsAdapter(shopsList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(shopAdapter);
    }
    private void prepareShopsData(List<Shops> shops) {
        for(Shops shop : shops)
            shopsList.add(shop);
        shopAdapter.notifyDataSetChanged();
    }

    private void initialProducts(View view) {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewProduct = view.findViewById(R.id.recyclerView_products);
        productAdapter = new ProductAdapter(productsList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(productAdapter);
    }
    private void prepareProductsData(List<Products> products) {
        for(Products product : products)
            productsList.add(product);
        productAdapter.notifyDataSetChanged();
    }

    private void initialSoldProducts(View view) {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewProduct = view.findViewById(R.id.recyclerView_solds);
        soldAdapter = new SoldAdapter(soldList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(soldAdapter);
    }
    private void prepareSoldProductsData(List<soldProduct> products) {
        soldText.setVisibility(View.VISIBLE);
        line_4.setVisibility(View.VISIBLE);
        for(soldProduct product : products)
            soldList.add(product);
        soldAdapter.notifyDataSetChanged();
    }

    private void initialCategories(View view) {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewProduct = view.findViewById(R.id.recyclerView_categories);
        categoryAdapter = new CategoryAdapter(categoryList,getContext(),"home");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(categoryAdapter);
    }
    private void prepareCategoriesData(List<Category> categorylist) {
        if(!categorylist.isEmpty())
            categoryList.add(new Category("العروض"));
        for(Category category : categorylist)
            categoryList.add(category);
        categoryAdapter.notifyDataSetChanged();
    }

    private void initialOfferProducts(View view) {
        RecyclerView recyclerViewProduct = view.findViewById(R.id.recyclerView_offers);
        offerAdapter = new OfferAdapter(offerList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(offerAdapter);
    }

    private void prepareOfferProductsData(List<Offer> products) {
        offerText.setVisibility(View.VISIBLE);
        line_3.setVisibility(View.VISIBLE);
        for(Offer product : products)
            offerList.add(product);
        offerAdapter.notifyDataSetChanged();
    }

    private void fullImageSlider(List<Ads> ads) {
        for (Ads ad:ads){
            slideModels.add(new SlideModel(ad.getImage_url().replace("https","http"),ScaleTypes.FIT));
        }
        slider.setImageList(slideModels,ScaleTypes.FIT);

        slider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                if(!Ads.get(i).getLink().startsWith("[") && !Ads.get(i).getLink().startsWith("#")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Ads.get(i).getLink()));
                    startActivity(browserIntent);
                }
                else if(Ads.get(i).getLink().startsWith("[product")){
                    String id = Ads.get(i).getLink();
                    id = id.substring(id.indexOf(":") + 1);
                    id = id.substring(0, id.indexOf("]"));

                    Intent intent = new Intent(getActivity(), ProductActivity.class);
                    intent.putExtra("ID", id);
                    startActivity(intent);
                }
                else if(Ads.get(i).getLink().startsWith("[store")){
                    String name = Ads.get(i).getLink();
                    name = name.substring(name.indexOf(":") + 1);
                    name = name.substring(0, name.indexOf("]"));

                    Intent intent = new Intent(getActivity(), ShopActivity.class);
                    intent.putExtra("store", name);
                    for (Shops shop:shopsList){
                        if(shop.getName().equals(name)) {
                            intent.putExtra("logo", shop.getLogo());
                            break;
                        }
                    }
                    startActivity(intent);
                }
            }
        });

        /*slider.setTouchListener(new TouchListener() {
            @Override
            public void onTouched(@NotNull ActionTypes actionTypes) {
                if (actionTypes == ActionTypes.DOWN){
                    slider.stopSliding();
                } else if (actionTypes == ActionTypes.UP ) {
                    slider.startSliding(3000);
                }
                //slider.stopSliding();
            }
        });*/
    }

    public List<Products> getProductsFromString(String result){

        List<Products> products=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Products pro=new Products(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4],oneObject[5],oneObject[6],
                        oneObject[7],oneObject[8],oneObject[9],oneObject[10],oneObject[11]);
                //pro.setPrice(oneObject[6]+"  ل.س");
                products.add(pro);
            }

        }
        return products;
    }

    public List<soldProduct> getSoldProductsFromString(String result){

        List<soldProduct> products=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                soldProduct pro=new soldProduct(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4],oneObject[5],oneObject[6],
                        oneObject[7],oneObject[8],oneObject[9],oneObject[10],oneObject[11],oneObject[12],oneObject[13]);
                products.add(pro);
            }

        }
        return products;
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

    public List<Offer> getOfferProductsFromString(String result){

        List<Offer> products=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Offer pro=new Offer(oneObject[0],oneObject[1],oneObject[2],oneObject[3],oneObject[4],oneObject[5],oneObject[6],
                        oneObject[7],oneObject[8],oneObject[9],oneObject[10],oneObject[11]);
                products.add(pro);
            }

        }
        return products;
    }

    public List<Ads> getAdsFromString(String result){

        Ads=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                Ads ad=new Ads(oneObject[0],oneObject[1]);
                Ads.add(ad);
            }

        }
        return Ads;
    }
    public List<Category> getCategoriesFromString(String result){

        List<Category> category=new ArrayList<>();
        String[] objectsString=result.split(";");
        for(int i=0;i<objectsString.length;i++) {
            Category cat = new Category(objectsString[i]);
            category.add(cat);
        }
        return category;
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

}

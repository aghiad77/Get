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
import com.example.aghiad_pc.get.Adapter.OfferAdapter;
import com.example.aghiad_pc.get.Adapter.ProductAdapter;
import com.example.aghiad_pc.get.Adapter.ShopsAdapter;
import com.example.aghiad_pc.get.Adapter.subCategoryAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Offer;
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

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OfferFragment extends Fragment {


    private BroadcastReceiver showCategories = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String categories = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_CATEGORIES_HOME_FRAGMENT);
            if(!categories.equals("") && categories!=null)
                prepareCategoriesData(getCategoriesFromString(categories));
        }
    };

    private BroadcastReceiver showProducts = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String products = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT);
            if(!products.equals("") && products!=null) {
                initialProducts();
                emptyText.setVisibility(view.INVISIBLE);
                prepareProductsData(getProductsFromString(products));
            }else{
                emptyText.setVisibility(view.VISIBLE);
            }
        }
    };


    private BroadcastReceiver showsubCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String subcategories = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SUBCATEGORIES_SHOP_FRAGMENT);
            if(!subcategories.equals("") && subcategories!=null) {
                preparesubCategoriesData(getsubCategoriesFromString(subcategories));
            }
        }
    };

    private BroadcastReceiver showProductsOffer = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String productsOffer = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT);
            if(!productsOffer.equals("") && productsOffer!=null){
                initialOfferProducts();
                emptyText.setVisibility(view.VISIBLE);
                prepareOfferProductsData(getOfferProductsFromString(productsOffer));
            }else{
                emptyText.setVisibility(view.VISIBLE);
            }
        }
    };

    private BroadcastReceiver getsubCtegories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String categoryS = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES);
            if(!categoryS.equals("") && categoryS!=null) {
                category = categoryS;
                emptyText.setVisibility(view.INVISIBLE);
                if(subCategoryList!=null && subCategoryList.size()!=0){
                    subCategoryList.clear();
                    subcategoryAdapter.notifyDataSetChanged();
                }
                if(productList!=null && productList.size()!=0){
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                }
                if(offerList!=null && offerList.size()!=0){
                    offerList.clear();
                    offerAdapter.notifyDataSetChanged();
                }
                if(!category.equals("العروض") && !category.equals("الكل")) {
                    CallGetSubCategories(category);
                }else if(category.equals("العروض")) {
                    CallGetOfferProducts();
                    subCategory = "all";
                }else if(category.equals("الكل")){
                    CallGetProducts(" ; ", "");
                    category = "";
                    subCategory = "all";
                }
            }
        }
    };

    private BroadcastReceiver getproductsBySubCategory = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String subcategoryS = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES_BY_SUBCATEGORY);
            if(subcategoryS!=null) {
                subCategory = subcategoryS;
                emptyText.setVisibility(view.INVISIBLE);
                if(productList!=null && productList.size()!=0){
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                }
                CallGetProducts(category+";"+subCategory,"");
            }
        }
    };

    private DrawerLayout mDrawer;
    NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView recyclerViewProduct , recyclerViewCategories;
    private static String category="" , subCategory="all" , selectedCategory="";

    private List<Products> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    private List<Offer> offerList = new ArrayList<>();
    private OfferAdapter offerAdapter;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    private List<subCategory> subCategoryList = new ArrayList<>();
    private subCategoryAdapter subcategoryAdapter;

    private Category category_all=new Category("العروض");
    private Category all_category=new Category("الكل");
    private TextView cartNumber, title , emptyText;
    private EditText searchText;
    private ImageView search , cart;
    private RelativeLayout relaText;
    private static View view;

    public static OfferFragment newInstance() {
        OfferFragment fragment = new OfferFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_offers, container, false);
        initial(view);
        initialCategories(view);
        initialsubCategories(view);
        CallServiceReceiver();


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
                //CallGetProducts(category+";"+subCategory,searchText.getText().toString());
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    dismissKeyboard(getActivity());
                    CallGetProducts(category+";"+subCategory,searchText.getText().toString());
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
                    CallGetProducts(category+";"+subCategory,searchText.getText().toString());
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
        emptyText = (TextView) view.findViewById(R.id.hintempty);
        searchText = (EditText) view.findViewById(R.id.edittext);
        search = (ImageView) view.findViewById(R.id.image_search);
        cart = (ImageView) view.findViewById(R.id.cart_image);
        relaText = (RelativeLayout) view.findViewById(R.id.textrel);
        recyclerViewProduct = view.findViewById(R.id.recyclerView_products);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        emptyText.setTypeface(face);
        //relaText.setVisibility(view.INVISIBLE);
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_GATEGORIES_HOME_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showProductsOffer, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(getsubCtegories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_FOR_STORES));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showsubCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_SUBGATEGORIES_FOR_STORES));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(getproductsBySubCategory, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_BY_SUBGATEGORIES_FOR_STORES));
        GetUtilities.startgetService(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            selectedCategory= args.getString("name");
        }
    }

    private void CallServiceReceiver() {
        if(categoryList!=null && categoryList.isEmpty()) {
            CallGetCategories();
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showCategories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showProductsOffer);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showProducts);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showCategories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(getsubCtegories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showsubCategories);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(getproductsBySubCategory);
        selectedCategory="";
        super.onPause();
    }

    private void setCartNumber() {
        if(getService.cartCounter>0)
            cartNumber.setText(Integer.toString(getService.cartCounter));
        else
            cartNumber.setText(" ");
    }

    private void CallGetCategories() {
        Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_CATEGORY);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME, "all");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent_);
    }
    private void CallGetSubCategories(String category) {
        Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_SUBCATEGORY);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME, "");
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_CATEGORY_NAME, category);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent_);
    }
    private void CallGetOfferProducts() {
        category = category_all.getName();
        Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_Offer);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE, "");
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE_SEARCH, "");
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent_);
    }
    private void CallGetProducts(String cat,String search) {

        if(!category.equals("العروض")){
            if(productList!=null && !productList.isEmpty()){
                productList.clear();
                productAdapter.notifyDataSetChanged();
            }
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_TOSTORE);
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE, "");
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_CATEGORY, cat);
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_SEARCH, search);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }else{
            if (offerList!=null && !offerList.isEmpty()){
                offerList.clear();
                offerAdapter.notifyDataSetChanged();
            }
            Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_Offer);
            intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE, "");
            intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE_SEARCH, search);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent_);
        }
    }


    private void initialCategories(View view) {
        // Add the following lines to create RecyclerView
        recyclerViewCategories = view.findViewById(R.id.recyclerView_categories);
        categoryAdapter = new CategoryAdapter(categoryList,getContext(),"category");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        //mLayoutManager.scrollToPositionWithOffset(2, 100);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewCategories.setLayoutManager(mLayoutManager);
        recyclerViewCategories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCategories.setAdapter(categoryAdapter);
        /*recyclerViewCategories.post(new Runnable() {
            @Override
            public void run() {
                recyclerViewCategories.smoothScrollToPosition(5);
            }
        });*/
        //listView.setSelection(3);
    }
    private void prepareCategoriesData(List<Category> category) {
        //categoryList.add(new Category(selectedCategory));
        categoryList.add(all_category);
        categoryList.add(category_all);
        for(Category cat : category)
            categoryList.add(cat);
        categoryAdapter.notifyDataSetChanged();
        if (!selectedCategory.equals("")) {
            if(selectedCategory.equals("العروض")) {
                scrollToPosition();
                categoryAdapter.notifyDataSetChanged();
                CallGetOfferProducts();
            }else {
                scrollToPosition();
                categoryAdapter.notifyDataSetChanged();
                GetSubCategories(selectedCategory);
            }
        }else{
            //CallGetOfferProducts();
            CallGetProducts(" ; ", "");
        }
    }

    private void scrollToPosition() {
        int position = categoryAdapter.getItemPosition(selectedCategory);
        if (position >= 0) {
            recyclerViewCategories.scrollToPosition(position);
        }
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

    public void GetSubCategories (String categoryS){
        if(!categoryS.equals("") && categoryS!=null) {
            category = categoryS;
            if(subCategoryList!=null && subCategoryList.size()!=0){
                subCategoryList.clear();
                subcategoryAdapter.notifyDataSetChanged();
            }
            if(productList!=null && productList.size()!=0){
                productList.clear();
                productAdapter.notifyDataSetChanged();
            }
            if(!category.equals("العروض"))
                CallGetSubCategories(category);
            else {
                if(offerList!=null && offerList.size()!=0){
                    offerList.clear();
                    offerAdapter.notifyDataSetChanged();
                }
                CallGetOfferProducts();
            }
        }
    }

    private void initialsubCategories(View view) {

        RecyclerView recyclerViewsubCategory = view.findViewById(R.id.recyclerView_subCategories);
        subcategoryAdapter = new subCategoryAdapter(subCategoryList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewsubCategory.setLayoutManager(mLayoutManager);
        recyclerViewsubCategory.setItemAnimator(new DefaultItemAnimator());
        recyclerViewsubCategory.setAdapter(subcategoryAdapter);
    }
    private void preparesubCategoriesData(List<subCategory> subcategory) {
        subCategoryList.add(new subCategory("الكل"));
        for(subCategory subcat : subcategory)
            subCategoryList.add(subcat);
        subcategoryAdapter.notifyDataSetChanged();
        CallGetProducts(category+";"+" ","");
    }
    public List<subCategory> getsubCategoriesFromString(String result){

        List<subCategory> subcategory=new ArrayList<>();
        String[] objectsString=result.split(";");
        for(int i=0;i<objectsString.length;i++) {
            subCategory cat = new subCategory(objectsString[i]);
            subcategory.add(cat);
        }
        return subcategory;
    }

    private void initialProducts() {
        // Add the following lines to create RecyclerView
        productAdapter = new ProductAdapter(productList,getContext());
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(productAdapter);
    }
    private void prepareProductsData(List<Products> products) {
        for(Products product : products)
            productList.add(product);
        productAdapter.notifyDataSetChanged();
        if(productList.isEmpty())
            emptyText.setVisibility(view.VISIBLE);
        else
            emptyText.setVisibility(View.INVISIBLE);
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

    private void initialOfferProducts() {
        // Add the following lines to create RecyclerView
        offerAdapter = new OfferAdapter(offerList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(offerAdapter);
    }
    private void prepareOfferProductsData(List<Offer> products) {
        for(Offer product : products)
            offerList.add(product);
        offerAdapter.notifyDataSetChanged();
        if(offerList.isEmpty())
            emptyText.setVisibility(View.VISIBLE);
        else
            emptyText.setVisibility(View.INVISIBLE);
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



    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }
}



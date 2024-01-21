package com.example.aghiad_pc.get.UI;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.aghiad_pc.get.Adapter.CategoryAdapter;
import com.example.aghiad_pc.get.Adapter.OfferAdapter;
import com.example.aghiad_pc.get.Adapter.ProductAdapter;
import com.example.aghiad_pc.get.Adapter.SoldAdapter;
import com.example.aghiad_pc.get.Adapter.subCategoryAdapter;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.Model.soldProduct;
import com.example.aghiad_pc.get.Model.subCategory;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;

import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {

    private BroadcastReceiver showStoresCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String category = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_CATEGORIES_SHOP_FRAGMENT);
            if(!category.equals("") && category!=null) {
                prepareCategoriesData(getCategoriesFromString(category));
            }
        }
    };

    private BroadcastReceiver showProductsOffer = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String productsOffer = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT);
            if(!productsOffer.equals("") && productsOffer!=null){
                initialOfferProducts();
                emptyText.setVisibility(View.INVISIBLE);
                prepareOfferProductsData(getOfferProductsFromString(productsOffer));
            }else{
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver showSoldProducts = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            final String soldproducts = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT);
            if(!soldproducts.equals("") && soldproducts!=null) {
                initialSoldProducts();
                emptyText.setVisibility(View.INVISIBLE);
                prepareSoldProductsData(getSoldProductsFromString(soldproducts));
            }else{
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver getsubCtegories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String categoryS = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES);
            if(!categoryS.equals("") && categoryS!=null) {
                category = categoryS;
                emptyText.setVisibility(View.INVISIBLE);
                if(subCategoryList!=null && subCategoryList.size()!=0){
                    subCategoryList.clear();
                    subcategoryAdapter.notifyDataSetChanged();
                }
                if(productList!=null && productList.size()!=0){
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                }
                if (soldList!=null && !soldList.isEmpty()){
                    soldList.clear();
                    soldAdapter.notifyDataSetChanged();
                }
                if(offerList!=null && offerList.size()!=0){
                    offerList.clear();
                    offerAdapter.notifyDataSetChanged();
                }
                if(!category.equals("العروض") && !category.equals("الكل") ) {
                    if(!category.equals("التخفيضات"))
                        CallGetSubCategories(storeName.getText().toString(), category);
                    else{
                        CallGetSoldProducts();
                        subCategory = "all";
                    }
                }else if(category.equals("العروض")) {
                    CallGetOfferProducts();
                    subCategory = "all";
                }else if(category.equals("الكل")){
                    CallGetProducts(storeName.getText().toString(), " ","all");
                    category = "";
                    subCategory = "all";
                }
            }
        }
    };

    private BroadcastReceiver showStoressubCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String subcategories = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SUBCATEGORIES_SHOP_FRAGMENT);
            if(!subcategories.equals("") && subcategories!=null) {
                preparesubCategoriesData(getsubCategoriesFromString(subcategories));
            }
        }
    };

    private BroadcastReceiver getproductsBySubCategory = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String subcategoryS = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_FOR_STORES_BY_SUBCATEGORY);
            if(!subcategoryS.equals("") && subcategoryS!=null) {
                subCategory = subcategoryS;
                emptyText.setVisibility(View.INVISIBLE);
                if(productList!=null && productList.size()!=0){
                    productList.clear();
                    productAdapter.notifyDataSetChanged();
                }
                CallGetProducts(storeName.getText().toString(), category,subCategory);
            }
        }
    };

    private BroadcastReceiver showProducts = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String products = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT);
            if(!products.equals("") && products!=null) {
                initialProducts();
                emptyText.setVisibility(View.INVISIBLE);
                prepareProductsData(getProductsFromString(products));
            }else{
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    };

    private TextView storeName , emptyText;
    private ImageView storeLogo , back;
    private RecyclerView recyclerViewProduct;
    private static String category , subCategory;

    private List<Products> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    private List<Offer> offerList = new ArrayList<>();
    private OfferAdapter offerAdapter;

    private List<soldProduct> soldList = new ArrayList<>();
    private SoldAdapter soldAdapter;

    private List<Category> categoryList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    private List<subCategory> subCategoryList = new ArrayList<>();
    private subCategoryAdapter subcategoryAdapter;

    private Category category_all=new Category("العروض");
    private Category all_category=new Category("الكل");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        storeName=(TextView) findViewById(R.id.store);
        storeLogo=(ImageView) findViewById(R.id.logo);
        emptyText = (TextView) findViewById(R.id.hintempty);
        back=(ImageView) findViewById(R.id.back);
        recyclerViewProduct = findViewById(R.id.recyclerView_products);
        Typeface face = Typeface.createFromAsset(this.getAssets(), "Tajawal-Medium.ttf");
        storeName.setTypeface(face);
        emptyText.setTypeface(face);
        getData();
        initialCategories();
        initialsubCategories();
        //initialProducts();
        CallGetCategories();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(showStoresCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES_CATEGORIES_SHOP_FRAGMENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(showProductsOffer, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(showStoressubCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_SUBGATEGORIES_FOR_STORES));
        LocalBroadcastManager.getInstance(this).registerReceiver(getsubCtegories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_FOR_STORES));
        LocalBroadcastManager.getInstance(this).registerReceiver(showProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(getproductsBySubCategory, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_BY_SUBGATEGORIES_FOR_STORES));
        LocalBroadcastManager.getInstance(this).registerReceiver(showSoldProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showStoresCategories);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showProductsOffer);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showStoressubCategories);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getsubCtegories);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showProducts);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(getproductsBySubCategory);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showSoldProducts);
        super.onPause();
    }

    private void getData() {
        try {
            storeName.setText(getIntent().getStringExtra("store"));
            new ImageDownloaderTask(storeLogo).execute(getIntent().getStringExtra("logo"));
        }catch (Exception e){
              return;
        }
    }

    private void CallGetCategories() {

        if(categoryList!=null && categoryList.isEmpty()) {
            Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_CATEGORY);
            intent_.putExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME, storeName.getText().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent_);
        }
    }
    private void CallGetOfferProducts() {
        Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_Offer);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE, storeName.getText().toString());
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE_SEARCH, "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent_);
    }
    private void CallGetSubCategories(String store, String category) {
        Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_SUBCATEGORY);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME, store);
        intent_.putExtra(GetStrings.MESSAGE_BROADCAST_CATEGORY_NAME, category);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent_);
    }
    private void CallGetProducts(String store, String category, String subCategory) {
        if(productList!=null && !productList.isEmpty()){
            productList.clear();
            productAdapter.notifyDataSetChanged();
        }else if (offerList!=null && !offerList.isEmpty()){
            offerList.clear();
            offerAdapter.notifyDataSetChanged();
        }
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_TOSTORE);
        if(store!=null && !store.equals(""))
            intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE, store);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_CATEGORY, category+";"+subCategory);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_SEARCH, "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    private void CallGetSoldProducts() {
        if(productList!=null && !productList.isEmpty()){
            productList.clear();
            productAdapter.notifyDataSetChanged();
        }else if (offerList!=null && !offerList.isEmpty()){
            offerList.clear();
            offerAdapter.notifyDataSetChanged();
        }else if (soldList!=null && !soldList.isEmpty()){
            soldList.clear();
            soldAdapter.notifyDataSetChanged();
        }
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS);
        intent.putExtra(GetStrings. MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_STORE, storeName.getText().toString());
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_SEARCH, " ");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

    private void initialCategories() {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewShops = findViewById(R.id.recyclerView_categories);
        categoryAdapter = new CategoryAdapter(categoryList,this,"shop");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(categoryAdapter);
    }
    private void prepareCategoriesData(List<Category> category) {
        categoryList.add(all_category);
        categoryList.add(category_all);
        categoryList.add(new Category("التخفيضات"));
        for(Category cat : category)
            categoryList.add(cat);
        categoryAdapter.notifyDataSetChanged();
        //CallGetOfferProducts();
        CallGetProducts(storeName.getText().toString(), " ","all");
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

    private void initialsubCategories() {

        RecyclerView recyclerViewsubCategory = findViewById(R.id.recyclerView_subCategories);
        subcategoryAdapter = new subCategoryAdapter(subCategoryList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
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
        CallGetProducts(storeName.getText().toString(),category,"all");
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
        productAdapter = new ProductAdapter(productList,this);
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
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
        offerAdapter = new OfferAdapter(offerList,this);
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
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

    private void initialSoldProducts() {
        // Add the following lines to create RecyclerView
        soldAdapter = new SoldAdapter(soldList,this);
        //GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(),2);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(soldAdapter);
    }
    private void prepareSoldProductsData(List<soldProduct> products) {
        for(soldProduct product : products)
            soldList.add(product);
        soldAdapter.notifyDataSetChanged();
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


}
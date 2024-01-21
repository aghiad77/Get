package com.example.aghiad_pc.get.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aghiad_pc.get.Adapter.ColorAdapter;
import com.example.aghiad_pc.get.Adapter.SizeAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Warehouse;
import com.example.aghiad_pc.get.Model.image;
import com.example.aghiad_pc.get.Model.productDetails;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.ImageDownloaderTask;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductActivity extends AppCompatActivity {

    private productDetails productDetails=new productDetails();

    private BroadcastReceiver showDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(getService.productDetails!=null && getService.productDetails.getProduct()!=null) {
                productDetails = getService.productDetails;
                fullData();
                //List<String> ss=sizeList;
            }
        }
    };

    private BroadcastReceiver addTOcartStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String status = intent.getStringExtra(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS_MESSAGE);
            if(status.equals("added")) {
                Toast.makeText(getApplication(),"تمت إضافة المنتج للسلة",Toast.LENGTH_LONG).show();
                counter=1;
                quantity.setText("1");
                setCartNumber();
                cost.setText(salary.getText().toString());
                addToCart.setEnabled(true);
            }else {
                new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setContentText(status)
                        .setConfirmText("متابعة")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
            }
            addToCart.setEnabled(true);
        }
    };

    private BroadcastReceiver sizeSelected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String size_Selected = intent.getStringExtra(GetStrings.ACTION_BROADCAST_SIZE_SELECTED_MESSAGE);
            if(!size_Selected.equals("") && size_Selected!=null) {
                size=size_Selected;
            }

        }
    };

    private BroadcastReceiver colorSelected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String color_Selected = intent.getStringExtra(GetStrings.ACTION_BROADCAST_COLOR_SELECTED_MESSAGE);
            if(!color_Selected.equals("") && color_Selected!=null) {
                variant=color_Selected;
                GetSizeForColor(variant);
                showColorImage(variant);
            }
        }
    };

    private BroadcastReceiver productQuantity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String quantity = intent.getStringExtra(GetStrings.ACTION_BROADCAST_PRODUCT_QUANTITY_MESSAGE);
            if(!quantity.equals("") && quantity!=null) {
                if(CheckForQuantity(Integer.parseInt(quantity))) {
                    Intent intent_ = new Intent(GetStrings.ACTION_BROADCAST_ADD_PRODUCT_TO_CART);
                    intent_.putExtra(GetStrings.MESSAGE_BROADCAST_ADD_PRODUCT_TO_CART, getproductToCart());
                    intent_.putExtra(GetStrings.MESSAGE_BROADCAST_STATUS_ADD_PRODUCT_TO_CART, "insert");
                    LocalBroadcastManager.getInstance(ProductActivity.this).sendBroadcast(intent_);
                }else{
                    Toast.makeText(getApplicationContext(),"الكمية المطلوبة أكبر من الكمية المتبقية وهي"+quantity+" طرد",Toast.LENGTH_LONG).show();
                    addToCart.setEnabled(true);
                }
            }
        }
    };

    private List<String> colorList = new ArrayList<>();
    private ColorAdapter colorAgapter;
    private List<String> sizeList = new ArrayList<>();
    private SizeAdapter sizeAgapter;
    private ImageView productImage,plus,minus,next,prev,favorite,cart,back;
    private Button addToCart;
    private TextView cost,quantity,name,title,salary,description,colorText,sizeText,quantityText,cartNumber,SyrianPound;
    private static int counter=1,imageIndex=0,warehouseQuantity=0;
    private String productID,size="",variant="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initial();
        getData();
        initialColors();
        initialSizes();
        CallServiceReceiver();

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart.setEnabled(false);
                if(Integer.parseInt(quantity.getText().toString())==0){
                    Toast.makeText(getApplicationContext(),"الرجاء تحديد الكمية المطلوبة",Toast.LENGTH_LONG).show();
                    addToCart.setEnabled(true);
                    return;
                }
                checkQuantity();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                quantity.setText(Integer.toString(counter));
                quantityText.setText(quantity.getText().toString() + "  طرد");
                if(counter==1)
                    cost.setText(salary.getText().toString());
                else {
                    int final_cost= Integer.parseInt(salary.getText().toString())*counter;
                    cost.setText(Integer.toString(final_cost));
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter>0)
                    counter--;
                quantity.setText(Integer.toString(counter));
                quantityText.setText(quantity.getText().toString() + "  طرد");
                if(counter==0 || counter==1)
                    cost.setText(salary.getText().toString());
                else{
                    int final_cost= Integer.parseInt(salary.getText().toString())*counter;
                    cost.setText(Integer.toString(final_cost));
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productDetails!=null && !productDetails.getImages().isEmpty()){
                    if(imageIndex<productDetails.getImages().size()-1){
                        imageIndex++;
                        next.setVisibility(View.VISIBLE);
                        prev.setVisibility(View.VISIBLE);
                        new ImageDownloaderTask(productImage).execute(productDetails.getImages().get(imageIndex).getSrc());
                        if(imageIndex==productDetails.getImages().size()-1) {
                            next.setVisibility(View.INVISIBLE);
                            prev.setVisibility(View.VISIBLE);
                           // imageIndex--;
                        }
                    }else{
                        next.setVisibility(View.INVISIBLE);
                        prev.setVisibility(View.VISIBLE);
                    }
                }else if(productDetails.getImages().isEmpty())
                    next.setVisibility(View.INVISIBLE);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(productDetails!=null && !productDetails.getImages().isEmpty()){
                   if(imageIndex>0){
                       prev.setVisibility(View.VISIBLE);
                       next.setVisibility(View.VISIBLE);
                       imageIndex--;
                       new ImageDownloaderTask(productImage).execute(productDetails.getImages().get(imageIndex).getSrc());
                       if(imageIndex==0){
                           prev.setVisibility(View.INVISIBLE);
                           next.setVisibility(View.VISIBLE);
                       }
                   }else {
                       //new ImageDownloaderTask(productImage).execute(productDetails.getProduct().getLogo());
                       prev.setVisibility(View.INVISIBLE);
                       next.setVisibility(View.VISIBLE);
                   }
                }else if(productDetails.getImages().isEmpty())
                    prev.setVisibility(View.INVISIBLE);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductActivity.this, HomeActivity.class);
                intent.putExtra("fragment","CartFragment");
                startActivity(intent);
                finish();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initial() {

        // TextView
        cost=(TextView) findViewById(R.id.final_cost);
        quantity=(TextView) findViewById(R.id.quantity_number);
        name=(TextView) findViewById(R.id.product_title);
        title=(TextView) findViewById(R.id.product_name);
        salary=(TextView) findViewById(R.id.product_price);
        SyrianPound=(TextView) findViewById(R.id.product_price_text);
        description=(TextView) findViewById(R.id.product_description);
        colorText=(TextView) findViewById(R.id.product_text_color);
        sizeText=(TextView) findViewById(R.id.product_text_size);
        quantityText=(TextView) findViewById(R.id.product_quantity);
        cartNumber = (TextView) findViewById(R.id.cart_count);

        // ImageView
        plus=(ImageView) findViewById(R.id.plus);
        minus=(ImageView) findViewById(R.id.minus);
        productImage=(ImageView) findViewById(R.id.productImage);
        next=(ImageView) findViewById(R.id.next);
        prev=(ImageView) findViewById(R.id.prev);
        favorite=(ImageView) findViewById(R.id.favorite);
        cart=(ImageView) findViewById(R.id.cart_image);
        back=(ImageView) findViewById(R.id.back);

        // Button
        addToCart=(Button) findViewById(R.id.button);

        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");

        // TextView TypeFace
        cost.setTypeface(face);
        quantity.setTypeface(face);
        name.setTypeface(face);
        title.setTypeface(face);
        salary.setTypeface(face);
        SyrianPound.setTypeface(face);
        description.setTypeface(face);
        colorText.setTypeface(face);
        sizeText.setTypeface(face);
        addToCart.setTypeface(face);
        quantityText.setTypeface(face);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(ProductActivity.this).unregisterReceiver(showDetails);
        LocalBroadcastManager.getInstance(ProductActivity.this).unregisterReceiver(addTOcartStatus);
        LocalBroadcastManager.getInstance(ProductActivity.this).unregisterReceiver(sizeSelected);
        LocalBroadcastManager.getInstance(ProductActivity.this).unregisterReceiver(colorSelected);
        LocalBroadcastManager.getInstance(ProductActivity.this).unregisterReceiver(productQuantity);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(ProductActivity.this).registerReceiver(showDetails, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_DETAILS));
        LocalBroadcastManager.getInstance(ProductActivity.this).registerReceiver(addTOcartStatus, new IntentFilter(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS));
        LocalBroadcastManager.getInstance(ProductActivity.this).registerReceiver(sizeSelected, new IntentFilter(GetStrings.ACTION_BROADCAST_SIZE_SELECTED));
        LocalBroadcastManager.getInstance(ProductActivity.this).registerReceiver(colorSelected, new IntentFilter(GetStrings.ACTION_BROADCAST_COLOR_SELECTED));
        LocalBroadcastManager.getInstance(ProductActivity.this).registerReceiver(productQuantity, new IntentFilter(GetStrings.ACTION_BROADCAST_PRODUCT_QUANTITY));
    }


    private void fullData() {
        new ImageDownloaderTask(productImage).execute(productDetails.getProduct().getLogo());
        if(productDetails.getProduct().getIs_favorite().equals("0"))
            favorite.setBackgroundResource(R.drawable.white_star);
        else
            favorite.setBackgroundResource(R.drawable.yellow_star);
        fullTextData();
        fullColorList();
        //fullSizeList();
    }
    private void fullTextData() {
        name.setText(productDetails.getProduct().getName());
        title.setText(productDetails.getProduct().getType());
        salary.setText(productDetails.getProduct().getPrice());
        SyrianPound.setVisibility(View.VISIBLE);
        description.setText(productDetails.getProduct().getComment());
        cost.setText(productDetails.getProduct().getPrice());
    }
    private void fullSizeList() {
         for(int i=0 ; i < productDetails.getWarehouse().size() ; i++){
             if(i==0)
                 sizeList.add(productDetails.getWarehouse().get(i).getSize());
             else {
                 if(!sizeList.contains(productDetails.getWarehouse().get(i).getSize()))
                     sizeList.add(productDetails.getWarehouse().get(i).getSize());
             }
         }
         if(sizeList!=null && !sizeList.isEmpty()){
             size=sizeList.get(0);
             prepareSizesData();
         }else{
             sizeText.setVisibility(View.INVISIBLE);
         }
    }

    private void showColorImage(String colorS) {
        for(image imag:productDetails.getImages()){
            if(imag.getColor().equals(colorS)){
                new ImageDownloaderTask(productImage).execute(imag.getSrc());
                break;
            }
        }
    }

    private void fullColorList() {
        for(int i=0 ; i < productDetails.getWarehouse().size() ; i++){
            if(i==0)
                colorList.add(productDetails.getWarehouse().get(i).getColor());
            else {
                if(!colorList.contains(productDetails.getWarehouse().get(i).getColor()))
                    colorList.add(productDetails.getWarehouse().get(i).getColor());
            }
        }
        if(colorList!=null && !colorList.isEmpty()){
            variant=colorList.get(0);
            prepareColorsData();
            GetSizeForColor(variant);
        }else{
            colorText.setVisibility(View.INVISIBLE);
        }
    }

    private void GetSizeForColor(String color) {
        if(!sizeList.isEmpty()){
            if(sizeList.size()==1 && sizeList.get(0).equals(""))
                sizeList.clear();
            else {
                sizeList.clear();
                prepareSizesData();
            }
        }
        for(int i=0 ; i < productDetails.getWarehouse().size() ; i++){
            if(color.equals(productDetails.getWarehouse().get(i).getColor())){
                if(sizeList.isEmpty())
                    sizeList.add(productDetails.getWarehouse().get(i).getSize());
                else{
                    if(!sizeList.contains(productDetails.getWarehouse().get(i).getSize()))
                        sizeList.add(productDetails.getWarehouse().get(i).getSize());
                }
            }
        }
        if(sizeList!=null && !sizeList.isEmpty()){
            if(sizeList.size()==1 && sizeList.get(0).equals("")) {
                sizeText.setVisibility(View.INVISIBLE);
                size=sizeList.get(0);
            }
            else {
                size = sizeList.get(0);
                prepareSizesData();
            }
        }else{
            sizeText.setVisibility(View.INVISIBLE);
        }
    }

    private void getData() {
        try {
            productID=getIntent().getStringExtra("ID");
        }catch (Exception e){
            //productID="";
        }
    }

    private void CallServiceReceiver() {

        // Call READ PRODUCT DETAILS
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_PRODUCT_DETAILS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_PRODUCT_DETAILS, productID);
        LocalBroadcastManager.getInstance(ProductActivity.this).sendBroadcast(intent);
    }

    private void initialSizes() {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewShops = findViewById(R.id.recyclerView_size);
        sizeAgapter = new SizeAdapter(sizeList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(sizeAgapter);
    }
    private void prepareSizesData() {
        sizeAgapter.notifyDataSetChanged();
    }

    private void initialColors() {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewShops = findViewById(R.id.recyclerView_color);
        colorAgapter = new ColorAdapter(colorList,this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewShops.setLayoutManager(mLayoutManager);
        recyclerViewShops.setItemAnimator(new DefaultItemAnimator());
        recyclerViewShops.setAdapter(colorAgapter);
    }
    private void prepareColorsData() {
        colorAgapter.notifyDataSetChanged();
    }

    private boolean CheckForQuantity(int count){
        if(Integer.parseInt(quantity.getText().toString()) <= count) {
            minusQuantity(size,variant,Integer.parseInt(quantity.getText().toString()));
            return true;
        }
        return false;
    }
    private void checkQuantity() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CHECK_PRODUCT_QUANTITY);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CHECK_QUANTITY, getproductQuantity());
        LocalBroadcastManager.getInstance(ProductActivity.this).sendBroadcast(intent);
    }
    private void minusQuantity(String size, String color,int quantity) {
        for(Warehouse warehouse:productDetails.getWarehouse()){
            if(warehouse.getColor().equals(color) && warehouse.getSize().equals(size))
                warehouse.setCount(String.valueOf(Integer.parseInt(warehouse.getCount())-quantity));
        }
    }

    private int getQuantityOfSizeandColor(String size, String color) {
        for(Warehouse warehouse:productDetails.getWarehouse()){
            if(warehouse.getColor().equals(color) && warehouse.getSize().equals(size)) {
                warehouseQuantity = Integer.parseInt(warehouse.getCount());
                return warehouseQuantity;
            }
        }
        return 0;
    }

    private String getproductToCart() {
        return productDetails.getProduct().getId()+";"+quantity.getText().toString()+";"
                +productDetails.getProduct().getPrice()+";"+variant+";"+size;

    }

    private String getproductQuantity() {
        return productDetails.getProduct().getId()+";"+variant+";"+size;

    }

    private void setCartNumber() {
        if(getService.cartCounter!=0)
            cartNumber.setText(Integer.toString(getService.cartCounter));
        else
            cartNumber.setText(" ");
    }

}

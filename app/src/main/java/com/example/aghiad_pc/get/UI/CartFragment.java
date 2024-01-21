package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.aghiad_pc.get.Adapter.CartAdapter;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.couponProduct;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import android.graphics.Color;

import static java.security.AccessController.getContext;

public class CartFragment extends Fragment {

    private BroadcastReceiver showConfirmOrderStatus = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CONFIRM_ORDER_STATUS_CART_FRAGMENT);
            final String msg = intent.getStringExtra(GetStrings. MESSAGE_BROADCAST_CONFIRM_ORDER_MSG_CART_FRAGMENT);

            if(status.equals("success")){
                Toast.makeText(getContext(),"تمت إضافة الطلبية بنجاح",Toast.LENGTH_LONG).show();
                clearData();
            }else if(status.equals("failed")){
                if(msg.equals("internal error"))
                    Toast.makeText(getContext(),"يوجد خطأ في السيرفر لا يمكن الطلب",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(),"تم نفاذ الكمية المطلوبة من المستودع الرجاء إعادة الطلب في حال وجود كمية أقل",Toast.LENGTH_LONG).show();
                button.setEnabled(true);
                code.setEnabled(true);
            }
        }
    };

    private BroadcastReceiver deleteproduct = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String msg = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_DELETE_PRODUCTS_CART_FRAGMENT);
            if(!msg.equals("") && msg!=null){
                if(msg.split(";")[1].equals("success")){
                    Toast.makeText(getContext(), "تم إزالة المنتج من السلة", Toast.LENGTH_SHORT).show();
                    refreshCartList(msg.split(";")[0],msg.split(";")[2],msg.split(";")[3]);
                }else {
                    Toast.makeText(getContext(), "لم يتم إزالة المنتج من السلة", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    private BroadcastReceiver getCartProducts = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String products = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_CART_PRODUCTS);
            if(products!=null && !products.equals("")) {
                prepareProductsData(getProductsFromString(products));
            }else{
                cartPrice.setVisibility(View.INVISIBLE);
                emptyText.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver getTransportCoast = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_COST);
            final String msg = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_COST_MSG);
            String coastMsg;
            if(msg.equals(""))
                coastMsg="سيترتب عليك أجور توصيل بقيمة"+ value + " ل.س";
            else
                coastMsg="سيترتب عليك أجور توصيل بقيمة "+ value + " ل.س، "+ msg;
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("هل ترغب في إكمال عملية التأكيد؟")
                    .setContentText(coastMsg)
                    .setConfirmText("تأكيد")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            CallServiceReceiver();
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .setCancelButton("رفض", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            button.setEnabled(true);
                            code.setEnabled(true);
                        }
                    })
                    .show();
        }
    };

    private BroadcastReceiver readCouponStatus = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent) {
            final String products = intent.getStringExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_PRICE_CART_FRAGMENT);
            final String msg = intent.getStringExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_MSG_CART_FRAGMENT);
            final int total_sold = Integer.parseInt(intent.getStringExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_TOTAL_SOLD_CART_FRAGMENT));
            new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                    .setContentText(msg)
                    .setConfirmText("متابعة")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
            int new_price = 0;
            if(products!="") {
                couponP = getCouponProductsFromString(products);
                for (couponProduct pro : couponP) {
                    new_price = new_price + (Integer.parseInt(pro.getNew_price()));
                }
                new_price = new_price - total_sold;
            }
            if(new_price < totalCost && new_price!=0){
                 totalnewpriceText.setVisibility(View.VISIBLE);
                 totalnewPrice.setVisibility(View.VISIBLE);
                 totalnewPrice.setText(new_price+  " ل.س");
            }
        }
    };


    private DrawerLayout mDrawer;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView title,totalPrice,cartNumber,totalnewPrice,totalpriceText,totalnewpriceText,emptyText;
    private Button button,code;
    private RelativeLayout cartPrice;
    private String codeValue="";
    private static int totalCost=0;
    private List<orderDetails> cartList = new ArrayList<>();
    private List<couponProduct> couponP = new ArrayList<>();
    private CartAdapter cartAdapter;
    private static View view;


    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initial(view);
        initialProducts(view);
        CallCartProducts();
        setCartNumber();
        RelativeLayout slideMenu=(RelativeLayout) view.findViewById(R.id.slide);

        slideMenu.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                mDrawer.openDrawer(Gravity.END);
            }
        });

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                button.setEnabled(false);
                code.setEnabled(false);
                CallCoastReceiver();
            }
        });

        code.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showcodeDialog();
            }
        });
        return view;
    }

    private void initial(View view) {

        title=(TextView) view.findViewById(R.id.cart_titile);
        totalPrice=(TextView) view.findViewById(R.id.totalPrice_number);
        totalnewPrice=(TextView) view.findViewById(R.id.totalnewPrice_number);
        cartNumber = (TextView) view.findViewById(R.id.cart_count);
        emptyText = (TextView) view.findViewById(R.id.hintText);
        cartPrice = (RelativeLayout) view.findViewById(R.id.cart_price);
        cartPrice.setVisibility(View.INVISIBLE);
        button=(Button) view.findViewById(R.id.button1);
        code=(Button) view.findViewById(R.id.code);
        totalnewpriceText=(TextView) view.findViewById(R.id.totalnewPrice_text);
        totalpriceText=(TextView) view.findViewById(R.id.totalPrice_text);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Tajawal-Medium.ttf");
        title.setTypeface(face);
        emptyText.setTypeface(face);
        //emptyText.setVisibility(view.INVISIBLE);
        button.setTypeface(face);
        code.setTypeface(face);
        totalPrice.setTypeface(face);
        totalpriceText.setTypeface(face);
        totalnewpriceText.setTypeface(face);
        totalnewPrice.setTypeface(face);
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
        //CallGetTransportCost();
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
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(showConfirmOrderStatus, new IntentFilter(GetStrings.ACTION_BROADCAST_CONFIRM_ORDER_STATUS_CART_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(deleteproduct, new IntentFilter(GetStrings.ACTION_BROADCAST_DELETE_PRODUCTS_CART_FRAGMENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(getCartProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_CART_PRODUCTS));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(getTransportCoast, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_COST));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(readCouponStatus, new IntentFilter(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_CART_FRAGMENT));
        GetUtilities.startgetService(getContext());
    }

    private void CallCartProducts() {
        if(cartList!=null && cartList.isEmpty()) {
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_IN_CART);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(showConfirmOrderStatus);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(deleteproduct);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(getCartProducts);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(getTransportCoast);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(readCouponStatus);
        super.onPause();
    }

    private void clearData() {
        cartList.clear();
        getService.productList.clear();
        cartAdapter.notifyDataSetChanged();
        calculatePrice();
        setCartNumber();
        cartPrice.setVisibility(View.INVISIBLE);
        emptyText.setVisibility(View.VISIBLE);
        GoToMyOrderActivity();
    }

    private void GoToMyOrderActivity() {
        Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
        startActivity(intent);
    }

    private void CallServiceReceiver() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CONFIRM_ORDER);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CONFIRM_ORDER, codeValue);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
    private void CallCoastReceiver() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_TRANSPORT_COST);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
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
    private void initialProducts(View view) {
        // Add the following lines to create RecyclerView
        RecyclerView recyclerViewProduct = view.findViewById(R.id.recyclerView_productCart);
        recyclerViewProduct.hasFixedSize();
        cartAdapter = new CartAdapter(cartList,getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(mLayoutManager);
        recyclerViewProduct.setItemAnimator(new DefaultItemAnimator());
        recyclerViewProduct.setAdapter(cartAdapter);
    }
    private void prepareProductsData(List<orderDetails> products) {
        for(orderDetails product:products)
            cartList.add(product);
        cartAdapter.notifyDataSetChanged();
        cartPrice.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.INVISIBLE);
        calculatePrice();
        setCartNumber();
    }
    private void refreshCartList(String id,String color,String size) {
        cartAdapter.notifyDataSetChanged();
        calculatePrice();
        calculateCouponPrice(id);
        setCartNumber();
        if(cartList.size()==0) {
            cartPrice.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCouponPrice(String id) {
        int new_price=0;
        for (couponProduct pro : couponP) {
            if(!pro.getProduct_id().equals(id))
                new_price = new_price + (Integer.parseInt(pro.getNew_price()));
            else
                couponP.remove(pro);
        }
        if(new_price < totalCost && new_price!=0){
            totalnewpriceText.setVisibility(View.VISIBLE);
            totalnewPrice.setVisibility(View.VISIBLE);
            totalnewPrice.setText(new_price+  " ل.س");
        }else {
            totalnewpriceText.setVisibility(View.INVISIBLE);
            totalnewPrice.setVisibility(View.INVISIBLE);
        }
    }
    private void calculatePrice() {
        totalCost=0;
        for (orderDetails pro : cartList){
            totalCost=totalCost+(Integer.parseInt(pro.getTotal_price()));
        }
        totalPrice.setText(totalCost+  "  ل.س");
    }
    private void setCartNumber() {
        if(cartList!=null && !cartList.isEmpty())
            cartNumber.setText(Integer.toString(cartList.size()));
        else
            cartNumber.setText("");
    }
    private void showcodeDialog() {
        final EditText editText = new EditText(getContext());
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.RED);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(3);
        editText.setBackground(shape);
        editText.setTextColor(Color.RED);
        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                .setTitleText("كـود الحسم")
                .setConfirmText("تأكيد")
                .setCancelText("خروج")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        codeValue = editText.getText().toString(); // variable to collect user input
                        CallReadCoupon();
                        sDialog.cancel();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        dialog.setCustomView(editText);
        dialog.show();
    }
    private void CallReadCoupon() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_READ_COUPON);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_READ_COUPON_CODE, codeValue);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }
    public List<orderDetails> getProductsFromString(String result){

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
    public List<couponProduct> getCouponProductsFromString(String result){

        List<couponProduct> products=new ArrayList<>();
        String[] objectsString=result.split("@result@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                couponProduct pro=new couponProduct(oneObject[0],oneObject[1],oneObject[2]);
                products.add(pro);
            }

        }
        return products;
    }
}

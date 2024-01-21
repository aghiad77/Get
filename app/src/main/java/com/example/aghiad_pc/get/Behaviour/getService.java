package com.example.aghiad_pc.get.Behaviour;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;
import com.example.aghiad_pc.get.Interface.Webservices;
import com.example.aghiad_pc.get.Model.Account;
import com.example.aghiad_pc.get.Model.Ads;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Contact;
import com.example.aghiad_pc.get.Model.Coupon;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.Favorite;
import com.example.aghiad_pc.get.Model.GetResponse;
import com.example.aghiad_pc.get.Model.Notification;
import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.Model.Product;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.Model.Shops;
import com.example.aghiad_pc.get.Model.Transport;
import com.example.aghiad_pc.get.Model.couponProduct;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.Model.productDetails;
import com.example.aghiad_pc.get.Model.soldProduct;
import com.example.aghiad_pc.get.Model.subCategory;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class getService extends Service {

    public static String session_id="";
    private static int productOffset=0,length=10,storeOffset=0,shopsprooffset=0,offerOffset=0,soldOffset=0,productCOffset=0;
    public static List<Product> productList=new ArrayList<>();
    public static productDetails productDetails;
    public static int cartCounter=0;
    public Database db;


    private BroadcastReceiver setSessienID = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String sessionID = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SESSION_ID);
            Runnable runableSetSessionId = new Runnable() {
                @Override
                public void run() {
                   session_id=sessionID;
                }
            };
            new Thread(runableSetSessionId).start();
        }
    };

    private BroadcastReceiver getProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String search = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SEARCH);
            Runnable runablegetProducts = new Runnable() {
                @Override
                public void run() {
                    CallProductsPostUrl(String.valueOf(length), String.valueOf(productOffset) , search);
                }
            };
            new Thread(runablegetProducts).start();
        }
    };

    private BroadcastReceiver getSoldProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String search = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_SEARCH);
            final String store = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_STORE);
            Runnable runablegetSoldProducts = new Runnable() {
                @Override
                public void run() {
                    CallSoldProductsPostUrl(String.valueOf(length), String.valueOf(soldOffset),search,store);
                }
            };
            new Thread(runablegetSoldProducts).start();
        }
    };

    private BroadcastReceiver getProductsOffer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String store = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE);
            final String search = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_STORE_SEARCH);
            Runnable runablegetProductsOffer = new Runnable() {
                @Override
                public void run() {
                    CallProductsOfferPostUrl(String.valueOf(length), String.valueOf(offerOffset),store,search);
                }
            };
            new Thread(runablegetProductsOffer).start();
        }
    };

    private BroadcastReceiver getStoreProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String store = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE);
            final String category = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_CATEGORY);
            final String search = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_STORE_SEARCH);
            Runnable runablegetProductsStores = new Runnable() {
                @Override
                public void run() {
                    if(!store.equals("")) {
                        shopsprooffset=0;
                        CallGetProductsPostUrl(String.valueOf(length), String.valueOf(shopsprooffset), store,
                                category.split(";")[0], category.split(";")[1], search);
                    } else {
                        productCOffset=0;
                        CallProductPostUrl(category.split(";")[0], category.split(";")[1], String.valueOf(length), String.valueOf(productCOffset), search);
                    }
                }
            };
            new Thread(runablegetProductsStores).start();
        }
    };

    private BroadcastReceiver getAllCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetallCategories = new Runnable() {
                @Override
                public void run() {
                    CallallCategoriesPostUrl();
                }
            };
            new Thread(runablegetallCategories).start();
        }
    };


    private BroadcastReceiver getStores = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String search = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SEARCH);
            Runnable runablegetStores = new Runnable() {
                @Override
                public void run() {
                    //storeOffset=0;
                    CallStoresPostUrl(String.valueOf(length), String.valueOf(storeOffset),search);
                }
            };
            new Thread(runablegetStores).start();
        }
    };

    private BroadcastReceiver getCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String storeName = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME);
            Runnable runablegetStoresCategories = new Runnable() {
                @Override
                public void run() {
                    if(!storeName.equals("all"))
                        CallCategoriesPostUrl(storeName);
                    else
                        CallallCategoriesPostUrl();
                }
            };
            new Thread(runablegetStoresCategories).start();
        }
    };

    private BroadcastReceiver getsubCategories = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String store = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_STORE_NAME);
            final String category = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CATEGORY_NAME);
            Runnable runablegetsubCategories = new Runnable() {
                @Override
                public void run() {
                    if(!store.equals(""))
                        CallsubCategoriesPostUrl(store,category);
                    else
                        CallsubCategoriesPostUrl(category);
                }
            };
            new Thread(runablegetsubCategories).start();
        }
    };

    private BroadcastReceiver confirmOrder = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String code = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CONFIRM_ORDER);
            Runnable runablegetconfirmOrder = new Runnable() {
                @Override
                public void run() {

                   // CallConfirmOrderPostUrl(products);
                    CallConfirmOrderPostUrl(code);
                }
            };
            new Thread(runablegetconfirmOrder).start();
        }
    };


    private BroadcastReceiver readCoupon = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String code = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_READ_COUPON_CODE);
            Runnable runablereadCoupon = new Runnable() {
                @Override
                public void run() {

                    // CallConfirmOrderPostUrl(products);
                    CallReadCouponPostUrl(code);
                }
            };
            new Thread(runablereadCoupon).start();
        }
    };


    private BroadcastReceiver getOrders = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetOrder = new Runnable() {
                @Override
                public void run() {
                    CallGetOrdesrPostUrl();
                }
            };
            new Thread(runablegetOrder).start();
        }
    };

    private BroadcastReceiver cancelOrder = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String orderID = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CANCEL_ORDER);
            Runnable runablecancelOrder = new Runnable() {
                @Override
                public void run() {

                    CallCancelOrderPostUrl(orderID);
                }
            };
            new Thread(runablecancelOrder).start();
        }
    };

    private BroadcastReceiver orderDetails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String orderId = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_GET_ORDER_DETAILS_ID);
            Runnable runableorderDetails = new Runnable() {
                @Override
                public void run() {
                    CallOrderDetailsPostUrl(orderId);
                }
            };
            new Thread(runableorderDetails).start();
        }
    };

    private BroadcastReceiver productDeatails = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String productID = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_PRODUCT_DETAILS);
            Runnable runableproductDeatails = new Runnable() {
                @Override
                public void run() {

                    CallProductDetailsPostUrl(productID);
                }
            };
            new Thread(runableproductDeatails).start();
        }
    };

    private BroadcastReceiver addProductToCart = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String product = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_ADD_PRODUCT_TO_CART);
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_STATUS_ADD_PRODUCT_TO_CART);
            Runnable runableaddProductToCart = new Runnable() {
                @Override
                public void run() {
                    if(status.equals("insert")) {
                        if (product.split(";").length == 5)
                            CallAddProductToCartPostUrl(product.split(";")[0], product.split(";")[1], product.split(";")[2], product.split(";")[3], product.split(";")[4]);
                        else
                            CallAddProductToCartPostUrl(product.split(";")[0], product.split(";")[1], product.split(";")[2], product.split(";")[3], "");
                        //ProductToCart(product);
                    }else {
                        /*Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_PRODUCTS_CART_FRAGMENT);
                        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_PRODUCTS_CART_FRAGMENT, getStringProductCart());
                        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);*/
                        CalldropProductToCartPostUrl(product.split(";")[0],product.split(";")[1],product.split(";")[2],product.split(";")[3]);
                    }
                }
            };
            new Thread(runableaddProductToCart).start();
        }
    };

    private BroadcastReceiver createFavorite = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String productID = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_ID);
            final String status = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CREATE_FAVORITE_STATUS);
            Runnable runablecreateFavorite = new Runnable() {
                @Override
                public void run() {

                    if(status.equals("create"))
                        CallcreateFavoritePostUrl(productID);
                    else
                        CalldropFavoritePostUrl(productID);
                }
            };
            new Thread(runablecreateFavorite).start();
        }
    };

    private BroadcastReceiver getFavorite = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetFavorite = new Runnable() {
                @Override
                public void run() {
                    CallGetFavoritePostUrl();
                }
            };
            new Thread(runablegetFavorite).start();
        }
    };

    private BroadcastReceiver getContact = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetContact = new Runnable() {
                @Override
                public void run() {
                    CallgetContactUrl();
                }
            };
            new Thread(runablegetContact).start();
        }
    };

    private BroadcastReceiver gettransportcost = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegettransportcost = new Runnable() {
                @Override
                public void run() {
                    CallGetTransportCostPostUrl();
                }
            };
            new Thread(runablegettransportcost).start();
        }
    };

    private BroadcastReceiver checkQuantity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String product = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_CHECK_QUANTITY);
            Runnable runablecheckQuantity = new Runnable() {
                @Override
                public void run() {
                    if(product.split(";").length==3)
                      CallCheckQuantityPostUrl(product.split(";")[0],product.split(";")[1],product.split(";")[2]);
                    else
                        CallCheckQuantityPostUrl(product.split(";")[0],product.split(";")[1],"");
                }
            };
            new Thread(runablecheckQuantity).start();
        }
    };

    private BroadcastReceiver getCartProducts = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetCartProducts = new Runnable() {
                @Override
                public void run() {
                    CallGetCartProductsPostUrl();
                }
            };
            new Thread(runablegetCartProducts).start();
        }
    };

    private BroadcastReceiver getAds = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetAds = new Runnable() {
                @Override
                public void run() {
                    CallGetAdsPostUrl();
                }
            };
            new Thread(runablegetAds).start();
        }
    };

    private BroadcastReceiver getNotifications = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetAds = new Runnable() {
                @Override
                public void run() {
                    CallGetNotificationsPostUrl();
                }
            };
            new Thread(runablegetAds).start();
        }
    };


    private BroadcastReceiver sendMsg = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String msg = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SEND_MSG);
            Runnable runableSendMsg = new Runnable() {
                @Override
                public void run() {
                    CallSendMsgPostUrl(msg);
                }
            };
            new Thread(runableSendMsg).start();
        }
    };

    private BroadcastReceiver getCondition = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetCondition = new Runnable() {
                @Override
                public void run() {
                    CallgetConditionPostUrl();
                }
            };
            new Thread(runablegetCondition).start();
        }
    };

    private BroadcastReceiver getAccount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Runnable runablegetAccount = new Runnable() {
                @Override
                public void run() {
                    CallgetAccountUrl();
                }
            };
            new Thread(runablegetAccount).start();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public void onCreate() {
        super.onCreate();
        db=new Database(this);
        if(db.getIsUserLogged())
            CallLoginPostUrl(db.getUser(),db.getPassword());
        LocalBroadcastManager.getInstance(this).registerReceiver(getProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS));
        LocalBroadcastManager.getInstance(this).registerReceiver(getSoldProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS));
        LocalBroadcastManager.getInstance(this).registerReceiver(setSessienID, new IntentFilter(GetStrings.ACTION_BROADCAST_SET_SESSION_ID));
        LocalBroadcastManager.getInstance(this).registerReceiver(getStores, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAllCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_ALL_CATEGORIES));
        LocalBroadcastManager.getInstance(this).registerReceiver(getCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES_CATEGORY));
        LocalBroadcastManager.getInstance(this).registerReceiver(getsubCategories, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_STORES_SUBCATEGORY));
        LocalBroadcastManager.getInstance(this).registerReceiver(confirmOrder, new IntentFilter(GetStrings.ACTION_BROADCAST_CONFIRM_ORDER));
        LocalBroadcastManager.getInstance(this).registerReceiver(getOrders, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_ORDERS));
        LocalBroadcastManager.getInstance(this).registerReceiver(cancelOrder, new IntentFilter(GetStrings.ACTION_BROADCAST_CANCEL_ORDER));
        LocalBroadcastManager.getInstance(this).registerReceiver(productDeatails, new IntentFilter(GetStrings.ACTION_BROADCAST_PRODUCT_DETAILS));
        LocalBroadcastManager.getInstance(this).registerReceiver(addProductToCart, new IntentFilter(GetStrings.ACTION_BROADCAST_ADD_PRODUCT_TO_CART));
        LocalBroadcastManager.getInstance(this).registerReceiver(getStoreProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_TOSTORE));
        LocalBroadcastManager.getInstance(this).registerReceiver(getProductsOffer, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_Offer));
        LocalBroadcastManager.getInstance(this).registerReceiver(createFavorite, new IntentFilter(GetStrings.ACTION_BROADCAST_CREATE_FAVORITE));
        LocalBroadcastManager.getInstance(this).registerReceiver(getFavorite, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_FAVORITE));
        LocalBroadcastManager.getInstance(this).registerReceiver(getContact, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_CONTACT));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAccount, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_ACCOUNT));
        LocalBroadcastManager.getInstance(this).registerReceiver(gettransportcost, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_TRANSPORT_COST));
        LocalBroadcastManager.getInstance(this).registerReceiver(orderDetails, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_ORDER_DETAILS));
        LocalBroadcastManager.getInstance(this).registerReceiver(checkQuantity, new IntentFilter(GetStrings.ACTION_BROADCAST_CHECK_PRODUCT_QUANTITY));
        LocalBroadcastManager.getInstance(this).registerReceiver(getCartProducts, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_IN_CART));
        LocalBroadcastManager.getInstance(this).registerReceiver(getAds, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_ADS));
        LocalBroadcastManager.getInstance(this).registerReceiver(readCoupon, new IntentFilter(GetStrings.ACTION_BROADCAST_READ_COUPON));
        LocalBroadcastManager.getInstance(this).registerReceiver(getNotifications, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_NOTIFICATIONS));
        LocalBroadcastManager.getInstance(this).registerReceiver(sendMsg, new IntentFilter(GetStrings.ACTION_BROADCAST_SEND_MESSAGE));
        LocalBroadcastManager.getInstance(this).registerReceiver(getCondition, new IntentFilter(GetStrings.ACTION_BROADCAST_GET_CONDITIONS));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getProducts);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(setSessienID);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getStores);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getCategories);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getsubCategories);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(confirmOrder);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getOrders);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(cancelOrder);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(productDeatails);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(addProductToCart);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getStoreProducts);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getProductsOffer);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(createFavorite);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getFavorite);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getContact);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getAccount);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(gettransportcost);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(orderDetails);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(checkQuantity);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getCartProducts);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getAds);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(readCoupon);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getSoldProducts);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getAllCategories);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getNotifications);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(sendMsg);
        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(getCondition);
        db.close();
        super.onDestroy();
    }


    // Call Login
    private void CallLoginPostUrl(String user, String password) {


        JSONObject JsonUser=new JSONObject();
        JsonUser.put("user", user);
        JsonUser.put("password", password);


        try {

            if (GetUtilities.isNetworkAvailable(this)) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.loginUser(JsonUser);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {

                        if (response.body().getStatus().equals("success")) {
                            session_id=response.body().getValue();
                            //Toast.makeText(getBaseContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();

                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
           // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

    }


    // Get all Products
    private void CallProductsPostUrl(String length , String offset , String search) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-products");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);
        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Products>> call = service.getProducts(JsonProduct);

                call.enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {

                        if(response.body().size()!=0) {
                            fullProducts(response.body(),search);
                            //LoadProductsImages(response.body());
                            //products=response.body();
                        }else{
                            productOffset=0;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Products>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullProducts(List<Products> result, String search) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_HOME_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_HOME_FRAGMENT, ConvertListProductsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            productOffset = productOffset + length;
            CallProductsPostUrl(String.valueOf(length), String.valueOf(productOffset),search);
        }
        if(result.size()<length){
            productOffset=0;
        }
    }
    private String ConvertListProductsToString(List<Products> result) {
        String products="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
               products=products+result.get(i).getStringObject(true);
            else
                products=products+result.get(i).getStringObject(false);
        }
        return products;
    }


    // Get all Sold Products
    private void CallSoldProductsPostUrl(String length, String offset, String search,String store) {
        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-products-sold");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);
        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);
        if(store!=null && !store.equals(" ") && !store.equals(""))
            JsonProduct.put("store", store);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<soldProduct>> call = service.getSolds(JsonProduct);

                call.enqueue(new Callback<List<soldProduct>>() {
                    @Override
                    public void onResponse(Call<List<soldProduct>> call, Response<List<soldProduct>> response) {

                        if(response.body().size()!=0) {
                            fullSoldProducts(response.body(),search,store);
                        }else {
                            fullSoldProducts(response.body(),search,store);
                            soldOffset=0;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<soldProduct>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullSoldProducts(List<soldProduct> result , String search , String store) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_SOLD_PRODUCTS_HOME_FRAGMENT, ConvertListSoldProductsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            soldOffset = soldOffset + length;
            CallSoldProductsPostUrl(String.valueOf(length), String.valueOf(soldOffset),search,store);
        }
        if(result.size()<length){
            soldOffset=0;
        }
    }
    private String ConvertListSoldProductsToString(List<soldProduct> result) {
        String soldproducts="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                soldproducts=soldproducts+result.get(i).getStringObject(true);
            else
                soldproducts=soldproducts+result.get(i).getStringObject(false);
        }
        return soldproducts;
    }

    // Get all Stores
    private void CallStoresPostUrl(String length , String offset , String search) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-stores");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);
        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);

        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Shops>> call = service.getStores(JsonProduct);

                call.enqueue(new Callback<List<Shops>>() {
                    @Override
                    public void onResponse(Call<List<Shops>> call, Response<List<Shops>> response) {

                        if(response.body().size()!=0) {
                            fullStores(response.body(),search);
                            //LoadStoresImages(response.body());
                            //products=response.body();
                        }else {
                            storeOffset=0;
                        }
                    }
                    @Override
                    public void onFailure(Call<List<Shops>> call, Throwable t) {
                        // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullStores(List<Shops> result, String search) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_HOME_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_HOME_FRAGMENT, ConvertListStoresToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            storeOffset = storeOffset + length;
            CallStoresPostUrl(String.valueOf(length), String.valueOf(storeOffset),search);
        }
        if(result.size()<length){
            storeOffset=0;
        }
    }
    private String ConvertListStoresToString(List<Shops> result) {
        String stores="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                stores=stores+result.get(i).getStringObject(true);
            else
                stores=stores+result.get(i).getStringObject(false);
        }
        return stores;
    }

    // Get All Categories
    private void CallallCategoriesPostUrl() {
        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-categories");
        JsonProduct.put("session_id", session_id);

//        Toast.makeText(getApplicationContext(),JsonProduct.toJSONString(),Toast.LENGTH_LONG).show();
        System.out.println(JsonProduct.toJSONString());

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Category>> call = service.getCategories(JsonProduct);

                call.enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                        if(response.body().size()!=0) {
                            fullallCategories(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                        //  Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullallCategories(List<Category> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_GATEGORIES_HOME_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_CATEGORIES_HOME_FRAGMENT, ConvertListCategoriesToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }



    // Get all Products Offer
    private void CallProductsOfferPostUrl(String length , String offset,String store, String search) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-products-offer");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);

        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);
        if(store!=null && !store.equals(" ") && !store.equals(""))
            JsonProduct.put("store", store);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Offer>> call = service.get_products_offers(JsonProduct);

                call.enqueue(new Callback<List<Offer>>() {
                    @Override
                    public void onResponse(Call<List<Offer>> call, Response<List<Offer>> response) {

                        if(response.body().size()!=0) {
                            fullProductsOffer(response.body(),store,search);
                        }else{
                            fullProductsOffer(response.body(),store,search);
                            offerOffset=0;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Offer>> call, Throwable t) {
                       // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullProductsOffer(List<Offer> result , String store , String search) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_OFFER_FRAGMENT, ConvertListProductsOfferToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            offerOffset = offerOffset + length;
            CallProductsOfferPostUrl(String.valueOf(length), String.valueOf(offerOffset),store,search);
        }
        if(result.size()<length){
            offerOffset=0;
        }
    }
    private String ConvertListProductsOfferToString(List<Offer> result) {
        String offer="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                offer=offer+result.get(i).getStringObject(true);
            else
                offer=offer+result.get(i).getStringObject(false);
        }
        return offer;
    }

    // Get Stores Categories
    private void CallCategoriesPostUrl(String store) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-categories-of-store");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("store", store);


        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Category>> call = service.get_store_categories(JsonProduct);

                call.enqueue(new Callback<List<Category>>() {
                    @Override
                    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {

                        if(response.body().size()!=0) {
                            fullCategories(response.body());
                            //products=response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Category>> call, Throwable t) {
                      //  Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullCategories(List<Category> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_STORES_CATEGORIES_SHOP_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_CATEGORIES_SHOP_FRAGMENT, ConvertListCategoriesToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListCategoriesToString(List<Category> result) {
        String categories="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                categories=categories+result.get(i).getStringObject(true);
            else
                categories=categories+result.get(i).getStringObject(false);
        }
        return categories;
    }

    // Get subCategories
    private void CallsubCategoriesPostUrl(String store,String category) {

        JSONObject JsonsubCategory=new JSONObject();
        JsonsubCategory.put("operation", "read-subcategories-of-store");
        JsonsubCategory.put("session_id", session_id);
        JsonsubCategory.put("store", store);
        JsonsubCategory.put("category", category);


        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<subCategory>> call = service.get_store_subcategories(JsonsubCategory);

                call.enqueue(new Callback<List<subCategory>>() {
                    @Override
                    public void onResponse(Call<List<subCategory>> call, Response<List<subCategory>> response) {

                        if(response.body().size()!=0) {
                            fullsubCategories(response.body());
                            //products=response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<subCategory>> call, Throwable t) {
                       // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
           // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
          //  e.printStackTrace();
            return;
        }
    }
    private void fullsubCategories(List<subCategory> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_SUBGATEGORIES_FOR_STORES);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_STORES_SUBCATEGORIES_SHOP_FRAGMENT, ConvertListsubCategoriesToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListsubCategoriesToString(List<subCategory> result) {
        String categories="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                categories=categories+result.get(i).getStringObject(true);
            else
                categories=categories+result.get(i).getStringObject(false);
        }
        return categories;
    }

    // Get subCategories
    private void CallsubCategoriesPostUrl(String category) {

        JSONObject JsonsubCategory=new JSONObject();
        JsonsubCategory.put("operation", "read-subcategories");
        JsonsubCategory.put("session_id", session_id);
        JsonsubCategory.put("category", category);


        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<subCategory>> call = service.get_store_subcategories(JsonsubCategory);

                call.enqueue(new Callback<List<subCategory>>() {
                    @Override
                    public void onResponse(Call<List<subCategory>> call, Response<List<subCategory>> response) {

                        if(response.body().size()!=0) {
                            fullsubCategories(response.body());
                            //products=response.body();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<subCategory>> call, Throwable t) {
                        // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //  e.printStackTrace();
            return;
        }
    }

    // Get Products For (Store and Category)
    private void CallGetProductsPostUrl(String length , String offset,String store,String category,String subCategory,String search) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-products");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);

        JsonProduct.put("store", store);

        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);

        if(category!=null && !category.equals(" ") && !category.equals(""))
            JsonProduct.put("category", category);

        if(subCategory!=null && !subCategory.equals(" ") && !subCategory.equals("") && !subCategory.equals("all"))
            JsonProduct.put("subcategory", subCategory);

        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Products>> call = service.get_products_sotre_category(JsonProduct);

                call.enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {

                        if(response.body().size()!=0) {
                            fullStoreProducts(response.body(),store,category,subCategory,search);
                            //LoadProductsImages(response.body());
                            //products=response.body();
                        }else {
                            fullStoreProducts(response.body(),store,category,subCategory,search);
                            shopsprooffset=0;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Products>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
           // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
          // e.printStackTrace();
            return;
        }
    }
    private void fullStoreProducts(List<Products> result,String store,String category,String subCategory,String search) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT, ConvertListProductsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            shopsprooffset = shopsprooffset + length;
            CallGetProductsPostUrl(String.valueOf(length), String.valueOf(shopsprooffset),store,category,subCategory,search);
        }
        if(result.size()<length){
            shopsprooffset=0;
        }
    }

    // Get all Products (Category & subCategory)
    private void CallProductPostUrl(String Category, String subCategory, String length , String offset , String search) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-products");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("length", length);
        JsonProduct.put("offset", offset);

        if(Category!=null && !Category.equals(" ") && !Category.equals(""))
            JsonProduct.put("category", Category);

        if(subCategory!=null && !subCategory.equals(" ") && !subCategory.equals("") && !subCategory.equals("all"))
            JsonProduct.put("subcategory", subCategory);

        if(search!=null && !search.equals(" ") && !search.equals(""))
            JsonProduct.put("search", search);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Products>> call = service.getProducts(JsonProduct);

                call.enqueue(new Callback<List<Products>>() {
                    @Override
                    public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {

                        if(response.body().size()!=0) {
                            fullProduct(response.body(),search);
                            //LoadProductsImages(response.body());
                            //products=response.body();
                        }else{
                            fullProduct(response.body(),search);
                            productCOffset=0;
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Products>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void fullProduct(List<Products> result, String search) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_PRODUCTS_SHOP_FRAGMENT, ConvertListProductToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        if(result.size()== length){
            productCOffset = productCOffset + length;
            CallProductsPostUrl(String.valueOf(length), String.valueOf(productCOffset),search);
        }
        if(result.size()<length){
            productCOffset=0;
        }
    }
    private String ConvertListProductToString(List<Products> result) {
        String products="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                products=products+result.get(i).getStringObject(true);
            else
                products=products+result.get(i).getStringObject(false);
        }
        return products;
    }

    private JSONArray getproductsArray(String products) {
        JSONArray Jsonproducts=new JSONArray();
        String[] objectsString=products.split("@product@");
        for(int i=0;i<objectsString.length;i++){
            String[] oneObject=objectsString[i].split(";");
            if(oneObject.length!=0 || oneObject!=null){
                JSONObject object= new JSONObject();
                object.put("product_id", oneObject[0]);
                object.put("count", oneObject[1]);
                object.put("price", oneObject[2]);
                object.put("size", oneObject[3]);
                object.put("variant", oneObject[4]);
                Jsonproducts.add(object);
            }
        }
        return Jsonproducts;
    }

    // Get Orders
    private void CallGetOrdesrPostUrl() {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-orders");
        JsonOrder.put("session_id", session_id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Orders>> call = service.getOrders(JsonOrder);

                call.enqueue(new Callback<List<Orders>>() {
                    @Override
                    public void onResponse(Call<List<Orders>> call, Response<List<Orders>> response) {
                        CallUseOders(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Orders>> call, Throwable t) {
                       // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseOders(List<Orders> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_ORDERS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ORDERS, ConvertListOrdersToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListOrdersToString(List<Orders> result) {
        String orders="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                orders=orders+result.get(i).getStringObject(true);
            else
                orders=orders+result.get(i).getStringObject(false);
        }
        return orders;
    }


    // Cancel order
    private void CallCancelOrderPostUrl(String orderID) {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "update-order-cancel");
        JsonOrder.put("session_id", session_id);
        JsonOrder.put("order_id", orderID);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.cancelorder(JsonOrder);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(response.body().getStatus().equals("success"))
                              SendCancelOrderStatus(response.body().getStatus(),orderID);
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                       // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
           //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void SendCancelOrderStatus(String status,String orderID) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_DELETE_ORDERS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS, orderID);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_ORDERS_STATUS, status);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Get order Details
    private void CallOrderDetailsPostUrl(String Id) {

        JSONObject Jsonproduct=new JSONObject();
        Jsonproduct.put("operation", "read-ordered-products");
        Jsonproduct.put("session_id", session_id);
        Jsonproduct.put("order_id", Id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<orderDetails>> call = service.getorderDetails(Jsonproduct);

                call.enqueue(new Callback<List<orderDetails>>() {
                    @Override
                    public void onResponse(Call<List<orderDetails>> call, Response<List<orderDetails>> response) {
                        CallUseOderDetails(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<orderDetails>> call, Throwable t) {
                      //  Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseOderDetails(List<orderDetails> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_ORDER_DETAILS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ORDER_DETAILS, ConvertListOrderDetailsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListOrderDetailsToString(List<orderDetails> result) {
        String orders="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                orders=orders+result.get(i).getStringObject(true);
            else
                orders=orders+result.get(i).getStringObject(false);
        }
        return orders;
    }


    // Get Product Details
    private void CallProductDetailsPostUrl(String Id) {

        JSONObject Jsonproduct=new JSONObject();
        Jsonproduct.put("operation", "read-product-details");
        Jsonproduct.put("session_id", session_id);
        Jsonproduct.put("product_id", Id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<productDetails> call = service.productDetails(Jsonproduct);

                call.enqueue(new Callback<productDetails>() {
                    @Override
                    public void onResponse(Call<productDetails> call, Response<productDetails> response) {
                        productDetails=response.body();
                        CallUseProductDetails();
                    }

                    @Override
                    public void onFailure(Call<productDetails> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseProductDetails() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_DETAILS);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Add Product To Cart
    private void ProductToCart(String product) {
        String[] object=product.split(";");
        if(object[8].equals("add")){

            Product pro=new Product(object[0],object[1],object[2],object[3],object[4],object[5],object[6],object[7]);
            if(!checkIfProductExist(pro))
                 productList.add(pro);
            Intent intent = new Intent(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS);
            intent.putExtra(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS_MESSAGE, "added");
            LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
        }else{
            for(Product pro : productList){
                if (pro.getId().equals(object[0]))
                    productList.remove(pro);
            }
        }
    }
    private String getStringProductCart(){
        String products="";
        for(int i=0;i<productList.size();i++){
            if(i<productList.size()-1)
                products=products+productList.get(i).getId()+";"+productList.get(i).getTitle()+";"+productList.get(i).getName()+";"+
                        productList.get(i).getSalary()+";"+productList.get(i).getImage()+";"+productList.get(i).getCount()+"@product@";
            else
                products=products+productList.get(i).getId()+";"+productList.get(i).getTitle()+";"+productList.get(i).getName()+";"+
                        productList.get(i).getSalary()+";"+productList.get(i).getImage()+";"+productList.get(i).getCount();
        }
        return products;
    }
    private boolean checkIfProductExist(Product product){
        if(productList!=null && !productList.isEmpty()){
            for(Product pro:productList){
                if(pro.getId().equals(product.getId()) && pro.getSize().equals(product.getSize()) && pro.getColor().equals(product.getColor())){
                    pro.setCount(String.valueOf(Integer.parseInt(pro.getCount())+Integer.parseInt(product.getCount())));
                    return true;
                }
            }
        }
        return false;
    }

    // Add or Delete Product To Favorite
    private void CallcreateFavoritePostUrl(String productID) {

        JSONObject JsonFavorite=new JSONObject();
        JsonFavorite.put("operation", "create-favorite");
        JsonFavorite.put("session_id", session_id);
        JsonFavorite.put("product_id", productID);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.createFavorite(JsonFavorite);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(!response.body().getStatus().equals("success"))
                            //Toast.makeText(getBaseContext(), "تم إضافة المنتج إلى المفضلة", Toast.LENGTH_SHORT).show();
                        //else
                            Toast.makeText(getBaseContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CalldropFavoritePostUrl(String productID) {

        JSONObject JsonFavorite=new JSONObject();
        JsonFavorite.put("operation", "drop-favorite");
        JsonFavorite.put("session_id", session_id);
        JsonFavorite.put("product_id", productID);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.cancelFavorite(JsonFavorite);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(!response.body().getStatus().equals("success"))
                            //Toast.makeText(getBaseContext(), "تم حذف المنتج من المفضلة", Toast.LENGTH_SHORT).show();
                        //else
                            Toast.makeText(getBaseContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }

    // Get Product Favorite
    private void CallGetFavoritePostUrl() {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-favorites");
        JsonOrder.put("session_id", session_id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Favorite>> call = service.getFavorite(JsonOrder);

                call.enqueue(new Callback<List<Favorite>>() {
                    @Override
                    public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                        fullFavorites(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Favorite>> call, Throwable t) {
                       // Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
           // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
           // e.printStackTrace();
            return;
        }
    }
    private void fullFavorites(List<Favorite> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_FAVORITE);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_FAVORITE, ConvertListFavoriteToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListFavoriteToString(List<Favorite> result) {
        String favorites="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                favorites=favorites+result.get(i).getStringObject(true);
            else
                favorites=favorites+result.get(i).getStringObject(false);
        }
        return favorites;
    }


    // Call get Contacts
    private void CallgetContactUrl() {

        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", "read-settings-contacts");
        JsonUser.put("session_id", session_id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<Contact> call = service.getContact(JsonUser);

                call.enqueue(new Callback<Contact>() {
                    @Override
                    public void onResponse(Call<Contact> call, Response<Contact> response) {
                         if(response.body()!=null)
                             sendContactDataToActivity(response.body());
                    }

                    @Override
                    public void onFailure(Call<Contact> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void sendContactDataToActivity(Contact body) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_CONTACT);
        intent.putExtra(GetStrings.ACTION_BROADCAST_SHOW_CONTACT_MESSAGE, body.getPhone()+";"+body.getAddress()+";"+body.getFacebook());
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Call get Account
    private void CallgetAccountUrl() {

        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", "read-userinfo");
        JsonUser.put("session_id", session_id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Account>> call = service.getAccount(JsonUser);

                call.enqueue(new Callback<List<Account>>() {
                    @Override
                    public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                        if(response.body()!=null)
                            sendAccountDataToActivity(response.body().get(0));
                    }

                    @Override
                    public void onFailure(Call<List<Account>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void sendAccountDataToActivity(Account body) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_ACCOUNT);
        intent.putExtra(GetStrings.ACTION_BROADCAST_SHOW_ACCOUNT_MESSAGE, body.getPhone()+";"+body.getName()+";"+body.getAddress());
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Call get Transport Cost
    private void CallGetTransportCostPostUrl() {

        JSONObject JsonCost=new JSONObject();
        JsonCost.put("operation", "read-order-shopping-transportcost");
        JsonCost.put("session_id", session_id);
        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<Transport> call = service.getTransportCost(JsonCost);

                call.enqueue(new Callback<Transport>() {
                    @Override
                    public void onResponse(Call<Transport> call, Response<Transport> response) {
                        if(response.body()!=null)
                            sendCostDataToActivity(response.body().getTransport_cost(),response.body().getTransport_message());
                    }

                    @Override
                    public void onFailure(Call<Transport> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e) {
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
    }
    private void sendCostDataToActivity(String cost,String msg) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_COST);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_COST, cost);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_COST_MSG, msg);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }


    // check warehouse quantity
    private void CallCheckQuantityPostUrl(String productID, String variant , String size) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-warehouse-count");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("product_id", productID);
        JsonProduct.put("variant", variant);
        JsonProduct.put("size", size);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.checkQuantity(JsonProduct);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(response.body().getStatus().equals("success"))
                            sendProductQuantityStatus(response.body().getValue());
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void sendProductQuantityStatus(String value){
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_PRODUCT_QUANTITY);
        intent.putExtra(GetStrings.ACTION_BROADCAST_PRODUCT_QUANTITY_MESSAGE, value);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Call add product to Cart
    private void CallAddProductToCartPostUrl(String productID, String count , String price, String variant , String size ) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "create-ordered-product");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("product_id", productID);
        JsonProduct.put("variant", variant);
        JsonProduct.put("size", size);
        JsonProduct.put("count", count);
        JsonProduct.put("price", price);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.addproducttocart(JsonProduct);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(response.body().getStatus().equals("success")) {
                            sendAddproductToCartStatus();
                            cartCounter++;
                        }else if(response.body().getStatus().equals("failed")){
                            showDialog(response.body().getMsg());
                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void showDialog(String msg) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS);
        intent.putExtra(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS_MESSAGE, msg);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private void sendAddproductToCartStatus(){
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS);
        intent.putExtra(GetStrings.ACTION_BROADCAST_ADD_TO_CART_STATUS_MESSAGE, "added");
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // drop product from Cart
    private void CalldropProductToCartPostUrl(String productID, String variant , String size , String count) {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "drop-ordered-product");
        JsonProduct.put("session_id", session_id);
        JsonProduct.put("product_id", productID);
        JsonProduct.put("variant", variant);
        JsonProduct.put("count", count);
        if(size.equals("null"))
            JsonProduct.put("size", "");
        else
            JsonProduct.put("size", size);

        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.dropproductfromcart(JsonProduct);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        sendDropProductFromCartStatus(productID,response.body().getStatus(),variant,size);
                        cartCounter--;
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void sendDropProductFromCartStatus(String id,String status,String variant,String size) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_DELETE_PRODUCTS_CART_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_DELETE_PRODUCTS_CART_FRAGMENT, id+";"+status+";"+variant+";"+size);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

    // Get Cart products
    private void CallGetCartProductsPostUrl() {

        JSONObject JsonProduct=new JSONObject();
        JsonProduct.put("operation", "read-ordered-products-shopping");
        JsonProduct.put("session_id", session_id);
        try {
            if (GetUtilities.isNetworkAvailable(this)) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<orderDetails>> call = service.getProductCart(JsonProduct);

                call.enqueue(new Callback<List<orderDetails>>() {
                    @Override
                    public void onResponse(Call<List<orderDetails>> call, Response<List<orderDetails>> response) {
                        CallUseCartProducts(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<orderDetails>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseCartProducts(List<orderDetails> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_CART_PRODUCTS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_GET_CART_PRODUCTS, ConvertListCartProductsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListCartProductsToString(List<orderDetails> result) {
        String orders="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                orders=orders+result.get(i).getStringObject(true);
            else
                orders=orders+result.get(i).getStringObject(false);
        }
        return orders;
    }

    // Confirm order
    private void CallConfirmOrderPostUrl(String coupon_code) {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "update-order-confirm");
        JsonOrder.put("session_id", session_id);
        JsonOrder.put("code", coupon_code);
        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.createOrder(JsonOrder);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        SendConfirmOrderStatus(response.body().getStatus(),response.body().getMsg());
                        if(response.body().getStatus().equals("success"))
                            cartCounter=0;
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void SendConfirmOrderStatus(String status,String Msg) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_CONFIRM_ORDER_STATUS_CART_FRAGMENT);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CONFIRM_ORDER_STATUS_CART_FRAGMENT, status);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_CONFIRM_ORDER_MSG_CART_FRAGMENT, Msg);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }


    // Get Ads
    private void CallGetAdsPostUrl() {
        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-ads-pages");
        JsonOrder.put("session_id", session_id);
        JsonOrder.put("page_name", "home");
        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Ads>> call = service.getAds(JsonOrder);

                call.enqueue(new Callback<List<Ads>>() {
                    @Override
                    public void onResponse(Call<List<Ads>> call, Response<List<Ads>> response) {
                        CallUseAds(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Ads>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                       // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseAds(List<Ads> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_ADS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_ADS, ConvertListAdsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListAdsToString(List<Ads> result) {
        String ads="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                ads=ads+result.get(i).getStringObject(true);
            else
                ads=ads+result.get(i).getStringObject(false);
        }
        return ads;
    }


    // Read Coupon
    private void CallReadCouponPostUrl(String coupon_code) {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-coupon");
        JsonOrder.put("session_id", session_id);
        JsonOrder.put("code", coupon_code);
        try {
            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<Coupon> call = service.readCoupon(JsonOrder);

                call.enqueue(new Callback<Coupon>() {
                    @Override
                    public void onResponse(Call<Coupon> call, Response<Coupon> response) {
                        SendReadCouponStatus(response.body().getProducts(), response.body().getMsg(),response.body().getTotal_sold());
                    }

                    @Override
                    public void onFailure(Call<Coupon> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void SendReadCouponStatus(List<couponProduct> products, String msg, String total) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_CART_FRAGMENT);
        intent.putExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_PRICE_CART_FRAGMENT, ConvertListCouponProductsToString(products));
        intent.putExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_MSG_CART_FRAGMENT, msg);
        intent.putExtra(GetStrings.ACTION_BROADCAST_READ_COUPON_STATUS_TOTAL_SOLD_CART_FRAGMENT, total);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListCouponProductsToString(List<couponProduct> result) {
        String products="";
        if(result==null)
            return products;
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                products=products+result.get(i).getStringObject(true);
            else
                products=products+result.get(i).getStringObject(false);
        }
        return products;
    }

    // Get Notifications
    private void CallGetNotificationsPostUrl() {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-notification");
        JsonOrder.put("session_id", session_id);

        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<List<Notification>> call = service.getNotifications(JsonOrder);

                call.enqueue(new Callback<List<Notification>>() {
                    @Override
                    public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                        CallUseNotifications(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<Notification>> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void CallUseNotifications(List<Notification> result) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_NOTIFICATIONS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_NOTIFICATIONS, ConvertListNotificationsToString(result));
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }
    private String ConvertListNotificationsToString(List<Notification> result) {
        String noti="";
        for(int i=0;i<result.size();i++){
            if(i<result.size()-1)
                noti=noti+result.get(i).getStringObject(true);
            else
                noti=noti+result.get(i).getStringObject(false);
        }
        return noti;
    }


    // Send Complaints and suggestions
    private void CallSendMsgPostUrl(String msg) {

        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "create-support-message");
        JsonOrder.put("session_id", session_id);
        JsonOrder.put("title", " ");
        JsonOrder.put("message", msg);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.sendSuggestions(JsonOrder);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        SendStatus(response.body().getStatus());
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void SendStatus(String status) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SEND_MSG_ACTIVITY);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SEND_RESULT, status);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }


    // Get Conditions
    private void CallgetConditionPostUrl() {
        JSONObject JsonOrder=new JSONObject();
        JsonOrder.put("operation", "read-settings-privacy");
        JsonOrder.put("session_id", session_id);
        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.getConditions(JsonOrder);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {
                        if(response.body().getStatus().equals("success"))
                            SendValue(response.body().getValue());
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        //Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        // t.printStackTrace();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            //Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
            return;
        }
    }
    private void SendValue(String status) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SHOW_CONDITIONS);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SHOW_CONDITIONS, status);
        LocalBroadcastManager.getInstance(getService.this).sendBroadcast(intent);
    }

}


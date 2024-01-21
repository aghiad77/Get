package com.example.aghiad_pc.get.Interface;

import com.example.aghiad_pc.get.Model.Account;
import com.example.aghiad_pc.get.Model.Ads;
import com.example.aghiad_pc.get.Model.Category;
import com.example.aghiad_pc.get.Model.Contact;
import com.example.aghiad_pc.get.Model.Coupon;
import com.example.aghiad_pc.get.Model.Favorite;
import com.example.aghiad_pc.get.Model.GetResponse;
import com.example.aghiad_pc.get.Model.Notification;
import com.example.aghiad_pc.get.Model.Offer;
import com.example.aghiad_pc.get.Model.Orders;
import com.example.aghiad_pc.get.Model.Products;
import com.example.aghiad_pc.get.Model.Shops;
import com.example.aghiad_pc.get.Model.Transport;
import com.example.aghiad_pc.get.Model.orderDetails;
import com.example.aghiad_pc.get.Model.productDetails;
import com.example.aghiad_pc.get.Model.soldProduct;
import com.example.aghiad_pc.get.Model.subCategory;

import org.json.simple.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Webservices {


    @POST("get-login.php")
    Call<GetResponse> loginUser(@Body JSONObject user);

    @POST("api.php")
    Call<GetResponse> signupUser(@Body JSONObject user);

    @POST("api.php")
    Call<List<Products>> getProducts(@Body JSONObject productBody);

    @POST("api.php")
    Call<List<Shops>> getStores(@Body JSONObject storeBody);

    @POST("api.php")
    Call<List<Category>> get_store_categories(@Body JSONObject categoryBody);

    @POST("api.php")
    Call<List<subCategory>> get_store_subcategories(@Body JSONObject subcategoryBody);

    @POST("api.php")
    Call<List<Offer>> get_products_offers(@Body JSONObject offerBody);

    @POST("api.php")
    Call<List<Products>> get_products_sotre_category(@Body JSONObject Body);

    @POST("api.php")
    Call<GetResponse> createOrder(@Body JSONObject orderBody);

    @POST("api.php")
    Call<List<Orders>> getOrders(@Body JSONObject orderBody);

    @POST("api.php")
    Call<GetResponse> cancelorder(@Body JSONObject orderBody);

    @POST("api.php")
    Call<List<orderDetails>>  getorderDetails(@Body JSONObject orderBody);

    @POST("api.php")
    Call<productDetails> productDetails(@Body JSONObject productBody);

    @POST("api.php")
    Call<GetResponse> createFavorite(@Body JSONObject favoriteBody);

    @POST("api.php")
    Call<List<Favorite>> getFavorite(@Body JSONObject favoriteBody);

    @POST("api.php")
    Call<GetResponse> cancelFavorite(@Body JSONObject favoriteBody);

    @POST("api.php")
    Call<Contact>  getContact(@Body JSONObject contactBody);

    @POST("api.php")
    Call<List<Account>>  getAccount(@Body JSONObject accountBody);

    @POST("api.php")
    Call<Transport>  getTransportCost(@Body JSONObject costBody);

    @POST("api.php")
    Call<GetResponse>  addproducttocart(@Body JSONObject productBody);

    @POST("api.php")
    Call<GetResponse>  checkQuantity(@Body JSONObject productBody);

    @POST("api.php")
    Call<GetResponse>  dropproductfromcart(@Body JSONObject productBody);

    @POST("api.php")
    Call<List<orderDetails>>  getProductCart(@Body JSONObject productBody);

    @POST("api.php")
    Call<List<Ads>>  getAds(@Body JSONObject adsBody);

    @POST("api.php")
    Call<Coupon>  readCoupon(@Body JSONObject couponBody);

    @POST("api.php")
    Call<List<soldProduct>>  getSolds(@Body JSONObject soldBody);

    @POST("api.php")
    Call<List<Category>> getCategories(@Body JSONObject categoryBody);

    @POST("api.php")
    Call<List<Notification>> getNotifications(@Body JSONObject notificationBody);

    @POST("api.php")
    Call<GetResponse> sendSuggestions(@Body JSONObject msg);

    @POST("api.php")
    Call<GetResponse> createCode(@Body JSONObject code);

    @POST("api.php")
    Call<GetResponse> confirmCode(@Body JSONObject code);

    @POST("api.php")
    Call<GetResponse> getConditions(@Body JSONObject condition);
}

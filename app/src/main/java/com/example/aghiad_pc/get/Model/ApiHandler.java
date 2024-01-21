package com.example.aghiad_pc.get.Model;

import com.example.aghiad_pc.get.Interface.Webservices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {

    public static final String BASE_URL = "http://getonline-sy.com/backend/";

    private static Webservices apiService;

    public static Webservices getApiService() {

        if (apiService == null) {

            Gson gson = new GsonBuilder().create();
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson)).baseUrl(BASE_URL).build();

            apiService = retrofit.create(Webservices.class);
            return apiService;
        } else {
            return apiService;
        }
    }


}

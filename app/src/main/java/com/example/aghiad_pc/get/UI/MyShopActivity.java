package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.myWebClient;

public class MyShopActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_shop);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://getonline-sy.com"));
        startActivity(browserIntent);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}

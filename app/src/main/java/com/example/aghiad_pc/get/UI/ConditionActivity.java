package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

public class ConditionActivity extends AppCompatActivity {


    private BroadcastReceiver showConditions = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String value = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SHOW_CONDITIONS);
            if(!value.equals("") && value!=null) {
               text.setText(value);
            }
        }
    };

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);
        text = (TextView) findViewById(R.id.text);
        text.setMovementMethod(new ScrollingMovementMethod());

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(showConditions, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_CONDITIONS));
        CallPostUrl();
    }

    private void CallPostUrl() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_CONDITIONS);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(showConditions);
        super.onPause();
    }
}

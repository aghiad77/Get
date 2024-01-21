package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

public class AccountActivity extends AppCompatActivity {

    private BroadcastReceiver showAccount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String account = intent.getStringExtra(GetStrings.ACTION_BROADCAST_SHOW_ACCOUNT_MESSAGE);
            showData(account);

        }
    };

    TextView user,home,name;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        user=(TextView)findViewById(R.id.user);
        home=(TextView)findViewById(R.id.home);
        name=(TextView)findViewById(R.id.name);

        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        user.setTypeface(face);
        home.setTypeface(face);
        name.setTypeface(face);

        LocalBroadcastManager.getInstance(this).registerReceiver(showAccount, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_ACCOUNT));
        CallgetContactUrl();


    }

    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(showAccount);
        super.onDestroy();
    }
    private void CallgetContactUrl() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_ACCOUNT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void showData(String account) {
        user.setText(account.split(";")[0]);
        home.setText(account.split(";")[2]);
        name.setText(account.split(";")[1]);
    }
}

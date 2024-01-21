package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

public class ContactActivity extends AppCompatActivity {

    private BroadcastReceiver showContact = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String contact = intent.getStringExtra(GetStrings.ACTION_BROADCAST_SHOW_CONTACT_MESSAGE);
            showData(contact);

        }
    };

    TextView phone,address;
    ImageView facebook;
    static String link="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        phone=(TextView)findViewById(R.id.phoneText);
        address=(TextView)findViewById(R.id.addressText);
        facebook=(ImageView) findViewById(R.id.faceImage);
        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        phone.setTypeface(face);
        address.setTypeface(face);
        //facebook.setTypeface(face);

        LocalBroadcastManager.getInstance(this).registerReceiver(showContact, new IntentFilter(GetStrings.ACTION_BROADCAST_SHOW_CONTACT));
        CallgetContactUrl();

        facebook.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                startActivity(browserIntent);
            }
        });
    }


    @Override
    public void onDestroy() {

        LocalBroadcastManager.getInstance(getApplication()).unregisterReceiver(showContact);
        super.onDestroy();
    }
    private void CallgetContactUrl() {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_GET_CONTACT);
        LocalBroadcastManager.getInstance(ContactActivity.this).sendBroadcast(intent);
    }

    private void showData(String contact) {
        phone.setText(contact.split(";")[0]);
        address.setText(contact.split(";")[1]);
        link = contact.split(";")[2];
    }


}

package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;

public class ComplaintsActivity extends AppCompatActivity {

    EditText text;
    Button send;

    private BroadcastReceiver sendResult = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String result = intent.getStringExtra(GetStrings.MESSAGE_BROADCAST_SEND_RESULT);
            if(result.equals("success")) {
                Toast.makeText(getApplicationContext(), "تم إرسال الرسالة بنجاح", Toast.LENGTH_LONG).show();
                text.setText("");
            }else
                Toast.makeText(getApplicationContext(),"لم يتم الإرسال، يوجد مشكلة",Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaints);
        text=(EditText) findViewById(R.id.editText);
        send=(Button) findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"يرجى كتابة المقترح أو الشكوى",Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SEND_MESSAGE);
                    intent.putExtra(GetStrings.MESSAGE_BROADCAST_SEND_MSG, text.getText().toString());
                    LocalBroadcastManager.getInstance(ComplaintsActivity.this).sendBroadcast(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(sendResult, new IntentFilter(GetStrings.ACTION_BROADCAST_SEND_MSG_ACTIVITY));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(sendResult);
        super.onPause();
    }
}
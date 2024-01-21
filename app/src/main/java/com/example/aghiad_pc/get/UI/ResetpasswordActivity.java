package com.example.aghiad_pc.get.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetUtilities;

import androidx.appcompat.app.AppCompatActivity;

public class ResetpasswordActivity extends AppCompatActivity {

    EditText phoneNum_edit;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpasword);

        initialize();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                if(inputVerify()) {
                    Intent intent = new Intent(ResetpasswordActivity.this, VerificationActivity.class);
                    intent.putExtra("user", phoneNum_edit.getText().toString());
                    intent.putExtra("from", "reset");
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void initialize() {

        button=(Button)findViewById(R.id.button);
        phoneNum_edit=(EditText)findViewById(R.id.editphonenumber);

    }

    private boolean inputVerify(){

        if (phoneNum_edit.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"الرجاء إدخال الرقم", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return false;
        }

        if (!GetUtilities.isValidMobile(phoneNum_edit.getText().toString())) {
            Toast.makeText(getApplicationContext(),"الرجاء إدخال رقم الهاتف الخاص بك مؤلف من 10 خانات ويبدأ بـ 0", Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return false;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}

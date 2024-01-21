package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
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

public class SignupActivity extends AppCompatActivity {

    EditText phoneNum_edit, password_edit, repassword_edit,userName_edit;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup1);

        initialize();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setEnable(false);
                if(inputVerify()) {
                    Intent intent = new Intent(SignupActivity.this, Signup2Activity.class);
                    intent.putExtra("user", phoneNum_edit.getText().toString());
                    intent.putExtra("password", password_edit.getText().toString());
                    intent.putExtra("name", userName_edit.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });

    }


    private void initialize() {

        button=(Button)findViewById(R.id.button);
        phoneNum_edit=(EditText)findViewById(R.id.editphonenumber);
        password_edit=(EditText)findViewById(R.id.editpassword);
        repassword_edit=(EditText)findViewById(R.id.editrepassword);
        userName_edit=(EditText)findViewById(R.id.editname);

        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");

        phoneNum_edit.setTypeface(face);
        password_edit.setTypeface(face);
        repassword_edit.setTypeface(face);
        userName_edit.setTypeface(face);
        button.setTypeface(face);

    }
    private void setEnable(boolean status) {

        button.setEnabled(status);
        phoneNum_edit.setEnabled(status);
        password_edit.setEnabled(status);
        repassword_edit.setEnabled(status);


    }
    private boolean inputVerify(){

        if (phoneNum_edit.getText().toString().equals("") || password_edit.getText().toString().equals("") ||
                repassword_edit.getText().toString().equals("") || userName_edit.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "الرجاء إدخال جميع البيانات", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        if (!GetUtilities.isValidMobile(phoneNum_edit.getText().toString())) {

            Toast.makeText(getBaseContext(), "يجب أن يكون اسم المستخدم رقم موبايل مؤلف من 10 أرقام", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        if (!password_edit.getText().toString().equals(repassword_edit.getText().toString())) {

            Toast.makeText(getBaseContext(), "كلمة المرور وتأكيدها غير متطابقتين", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        return true;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


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

package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Interface.Webservices;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.Model.GetResponse;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.simple.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNum_edit, password_edit;
    Button button;
    TextView  forget_text, create_text;
    String user,from,name,home;
    Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getData();
        initialize();
        subscribeTopic();
        startService();
        db=new Database(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                setEnable(false);
                if(inputVerify())
                    CallPostUrl(phoneNum_edit.getText().toString(),password_edit.getText().toString());
            }
        });

        forget_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetpasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        create_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        password_edit.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Drawable unwrappedDrawable = password_edit.getBackground();
                //unwrappedDrawable.setColorFilter(android.graphics.Color.parseColor("#516590"), PorterDuff.Mode.SRC_ATOP);
                //password_edit.setBackground(unwrappedDrawable);
                password_edit.setHintTextColor(R.color._________color);
                return false;
            }

        });
    }

    private void subscribeTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("get")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Get Topic is subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Get Topic is not subsucribed";
                        }
                        //Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {

        db.close();
        super.onDestroy();
    }

    private void CallPostUrl(String user, String password) {


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

                            GoToHome(response.body().getValue(),user,password,response.body().getMsg());

                        } else if(response.body().getStatus().equals("failed")) {

                            if(response.body().getMsg().equals("user is disabled"))
                                Toast.makeText(getBaseContext(), "الحساب معطل حالياً، يتم التحقق من قبل مدير النظام", Toast.LENGTH_SHORT).show();
                            if(response.body().getMsg().equals("user not exists"))
                                  Toast.makeText(getBaseContext(), "المستخدم غير موجود قم بإنشاء حساب جديد", Toast.LENGTH_SHORT).show();
                            if(response.body().getMsg().equals("password error")) {
                                Toast.makeText(getBaseContext(), "كلمة المرور خاطئة", Toast.LENGTH_SHORT).show();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    changeEditPassword();
                                }
                            }
                            setEnable(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        setEnable(true);
                        //t.printStackTrace();

                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
                setEnable(true);
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
            setEnable(true);
        }

    }
    private void GoToHome(String value,String user,String password,String type) {

        sendTokenToService(value);
        if(name==null)
            name="";
        if(home==null)
            home="";
        db.saveLogin(user,password,"logged",type,name,home);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void sendTokenToService(String value) {
        Intent intent = new Intent(GetStrings.ACTION_BROADCAST_SET_SESSION_ID);
        intent.putExtra(GetStrings.MESSAGE_BROADCAST_SESSION_ID, value);
        LocalBroadcastManager.getInstance(LoginActivity.this).sendBroadcast(intent);
    }

    private void getData() {
        try {
            user= getIntent().getStringExtra("user");
            name= getIntent().getStringExtra("name");
            home= getIntent().getStringExtra("home");
            from=getIntent().getStringExtra("from");
        }catch (Exception e){
            user="";
            from="";
            name="";
            home= "";
        }
    }
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void changeEditPassword() {

        Drawable unwrappedDrawable = password_edit.getBackground();
        unwrappedDrawable.setColorFilter(android.graphics.Color.parseColor("#D3FA0000"), PorterDuff.Mode.SRC_ATOP);
        password_edit.setBackground(unwrappedDrawable);
        password_edit.setHintTextColor(R.color.order_status_cancel);
    }


    private void initialize() {

        button=(Button)findViewById(R.id.button);
        forget_text=(TextView)findViewById(R.id.forgetpassword);
        create_text=(TextView)findViewById(R.id.create);

        phoneNum_edit=(EditText)findViewById(R.id.editphonenumber);
        password_edit=(EditText)findViewById(R.id.editpassword);
        if(!(from==null)) {
            if (from.equals("signup")) {
                phoneNum_edit.setText(user);
                phoneNum_edit.setEnabled(false);
            }
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");
        forget_text.setTypeface(face);
        create_text.setTypeface(face);
        button.setTypeface(face);
        phoneNum_edit.setTypeface(face);
        password_edit.setTypeface(face);
        //phoneNum_edit.setClickable(true);
        //password_edit.setClickable(true);

    }
    private void setEnable(boolean status) {

        button.setEnabled(status);
        //forget_text.setEnabled(status);
        create_text.setEnabled(status);
        phoneNum_edit.setEnabled(status);
        password_edit.setEnabled(status);

        if(!(from==null)) {
            if (from.equals("signup"))
                phoneNum_edit.setEnabled(false);
        }
    }
    private boolean inputVerify(){

        if (phoneNum_edit.getText().toString().equals("") || password_edit.getText().toString().equals("")) {

            Toast.makeText(getBaseContext(), "الرجاء إدخال جميع البيانات", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        if (!GetUtilities.isValidMobile(phoneNum_edit.getText().toString())) {

            Toast.makeText(getBaseContext(), "يجب أن يكون اسم المستخدم رقم موبايل مؤلف من 10 أرقام", Toast.LENGTH_SHORT).show();
            setEnable(true);
            return false;
        }

        return true;
    }

    private void startService(){
        try {
            //todo handle no internet connect with other way
            if (!GetUtilities.IsMyServiceRunning(this, getService.class)) {
                Intent i = new Intent(LoginActivity.this, getService.class);
                startService(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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


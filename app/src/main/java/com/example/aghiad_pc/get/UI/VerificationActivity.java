package com.example.aghiad_pc.get.UI;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aghiad_pc.get.Interface.Webservices;
import com.example.aghiad_pc.get.Model.GetResponse;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetUtilities;

import org.json.simple.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerificationActivity extends AppCompatActivity {


    EditText code_edit;
    Button button;
    String user,from,name,home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification);
        initialize();
        getData();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                if(inputVerify()){
                    if(from.equals("signup"))
                        CallConfirmCodePostUrl(user,"update-user-activate",getCode());
                    else
                        CallConfirmCodePostUrl(user,"confirm-password-code",getCode());
                    button.setEnabled(true);
                }else
                    button.setEnabled(true);
            }
        });

    }

    private void initialize() {

        button=(Button)findViewById(R.id.button);
        code_edit=(EditText) findViewById(R.id.code_1);

        //TextWatcher();
    }

    private void getData() {
        try {
            user= getIntent().getStringExtra("user");
            from=getIntent().getStringExtra("from");
            name=getIntent().getStringExtra("name");
            home=getIntent().getStringExtra("home");
            if(from.equals("signup"))
                CallPostUrl(user , "create-user-code-confirm");
            else
                CallPostUrl(user , "create-password-code-confirm");
        }catch (Exception e){
            user="";
            from="";
            name="";
            home= "";
        }
    }

    private void GoToHome() {
        if(from.equals("signup")) {
            Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
            intent.putExtra("user", user);
            intent.putExtra("name", name);
            intent.putExtra("home", home);
            intent.putExtra("from", "signup");
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(VerificationActivity.this, Resetpassword2Activity.class);
            intent.putExtra("user", user);
            intent.putExtra("code", getCode());
            startActivity(intent);
            finish();
        }
    }

    private String getCode() {
        return code_edit.getText().toString();
    }

    private void CallPostUrl(String user , String operation) {


        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", operation);
        JsonUser.put("user", user);

        try {
            if (GetUtilities.isNetworkAvailable(this)) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.createCode(JsonUser);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {

                        if (response.body().getStatus().equals("success")) {


                        } else if(response.body().getStatus().equals("error")) {

                            if(response.body().getValue().contains("Duplicate entry") && response.body().getValue().contains("for key 'user'"))
                                Toast.makeText(getBaseContext(), "المستخدم موجود مسبقاً", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void CallConfirmCodePostUrl(String user , String operation , String code) {

        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", operation);
        JsonUser.put("user", user);
        JsonUser.put("code", code);

        try {
            if (GetUtilities.isNetworkAvailable(this)) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.confirmCode(JsonUser);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {

                        if (response.body().getStatus().equals("success")) {
                             GoToHome();
                        } else if(response.body().getStatus().equals("error")) {

                            if(response.body().getMsg().equals("code error"))
                                Toast.makeText(getBaseContext(), "كود التفعيل غير صحيح!", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private boolean inputVerify(){

        if (code_edit.getText().toString().equals("")) {

            Toast.makeText(this,"الرجاء إدخال كود التفعيل بشكل كامل",Toast.LENGTH_LONG).show();
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

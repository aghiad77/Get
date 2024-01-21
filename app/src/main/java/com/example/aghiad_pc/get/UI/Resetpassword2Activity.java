package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aghiad_pc.get.Interface.Webservices;
import com.example.aghiad_pc.get.Model.GetResponse;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetUtilities;

import org.json.simple.JSONObject;

public class Resetpassword2Activity extends AppCompatActivity {

    EditText password_edit , confirm_edit;
    Button button;
    String user,code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword2);
        initialize();
        getData();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                if(inputVerify()) {
                    CallConfirmCodePostUrl(user,"update-user-password",code,password_edit.getText().toString());
                }
            }
        });
    }

    private void initialize() {
        button=(Button)findViewById(R.id.button);
        password_edit=(EditText)findViewById(R.id.editpassword);
        confirm_edit=(EditText)findViewById(R.id.editconfirmpassword);
    }

    private void getData() {
        try {
            user= getIntent().getStringExtra("user");
            code=getIntent().getStringExtra("code");
        }catch (Exception e){
            user="";
            code="";
        }
    }

    private void GoToHome() {
        Intent intent = new Intent(Resetpassword2Activity.this, LoginActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("from", "signup");
        startActivity(intent);
        finish();
    }

    private boolean inputVerify(){

        if (password_edit.getText().toString().equals("") || confirm_edit.getText().toString().equals("")) {

            Toast.makeText(this,"الرجاء إدخال بيانات الحقول كاملة!",Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return false;
        }

        if (!password_edit.getText().toString().equals(confirm_edit.getText().toString())) {

            Toast.makeText(this,"كلمة المرور وتأكيدها غير متطابقين!",Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return false;
        }

        return true;
    }

    private void CallConfirmCodePostUrl(String user , String operation , String code , String password) {

        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", operation);
        JsonUser.put("user", user);
        JsonUser.put("code", code);
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
}
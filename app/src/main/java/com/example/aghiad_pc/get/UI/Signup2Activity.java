package com.example.aghiad_pc.get.UI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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


public class Signup2Activity extends AppCompatActivity {

    EditText address_edit;
    Button button;
    Spinner city_spinner;
    String user,password,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup2);
        getData();
        initialize();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setEnabled(false);
                if(inputVerify())
                    CallPostUrl(user,password,name,city_spinner.getSelectedItem().toString(),address_edit.getText().toString());

            }
        });

    }

    private void getData() {
        try {
            user= getIntent().getStringExtra("user");
            password=getIntent().getStringExtra("password");
            name=getIntent().getStringExtra("name");
        }catch (Exception e){
            user="";
            password="";
            name="";
        }
    }


    private void CallPostUrl(String user, String password, String name,String province, String address) {


        JSONObject JsonUser=new JSONObject();
        JsonUser.put("operation", "create-user");
        JsonUser.put("user", user);
        JsonUser.put("password", password);
        JsonUser.put("name", name);
        JsonUser.put("province", province);
        JsonUser.put("address", address);


        try {

            if (GetUtilities.isNetworkAvailable(this)) {

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Your URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(GetUtilities.getUnsafeOkHttpClient().build())
                        //.addConverterFactory(ScalarsConverterFactory.create())
                        .build();

                final Webservices service = retrofit.create(Webservices.class);
                Call<GetResponse> call = service.signupUser(JsonUser);

                call.enqueue(new Callback<GetResponse>() {
                    @Override
                    public void onResponse(Call<GetResponse> call, Response<GetResponse> response) {

                        if (response.body().getStatus().equals("success")) {

                            if(response.body().getMsg().equals("verification code"))
                                GoToVerifiy();
                            else
                                GoToHome();

                        } else if(response.body().getStatus().equals("error")) {

                            if(response.body().getValue().contains("Duplicate entry") && response.body().getValue().contains("for key 'user'"))
                                Toast.makeText(getBaseContext(), "المستخدم موجود مسبقاً", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<GetResponse> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();
                        //t.printStackTrace();

                    }
                });
            }else {
                Toast.makeText(getBaseContext(), "لا يوجد اتصال انترنت تحقق من الشبكة", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    private void initialize() {

        button=(Button)findViewById(R.id.button);
        address_edit=(EditText)findViewById(R.id.editaddress);
        city_spinner=(Spinner) findViewById(R.id.cityspinner);

        address_edit.setClickable(true);
        fill_data_spinner();

        Typeface face = Typeface.createFromAsset(getAssets(), "Tajawal-Medium.ttf");

        address_edit.setTypeface(face);
        button.setTypeface(face);

    }
    private boolean inputVerify(){

        if (address_edit.getText().toString().equals("") || city_spinner == null
                || city_spinner.getSelectedItem() ==null || city_spinner.getSelectedItem().toString().equals("")) {

            Toast.makeText(this,"الرجاء إدخال العنوان",Toast.LENGTH_LONG).show();
            button.setEnabled(true);
            return false;
        }

        return true;
    }
    private void GoToHome() {

        Intent intent = new Intent(Signup2Activity.this, LoginActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("from", "signup");
        startActivity(intent);
        finish();
    }

    private void GoToVerifiy() {
        Intent intent = new Intent(Signup2Activity.this, VerificationActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("name", name);
        intent.putExtra("home", city_spinner.getSelectedItem().toString()+" - "+address_edit.getText().toString());
        intent.putExtra("from", "signup");
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    private void fill_data_spinner(){
        String[] arraySpinner = new String[] {
                "دمشق", "ريف دمشق", "حمص", "طرطوس", "اللاذقية","حماة", "حلب", "السويداء","درعا","القنيطرة",
                "الرقة","الحسكة","دير الزور","إدلب"};
        //city_spinner.setGravity(Gravity.RIGHT);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_list, arraySpinner);
        adapter.setDropDownViewResource(R.layout.spinner_list);
        city_spinner.setAdapter(adapter);
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

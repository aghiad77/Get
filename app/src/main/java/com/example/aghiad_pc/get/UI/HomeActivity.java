package com.example.aghiad_pc.get.UI;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class HomeActivity extends AppCompatActivity {

    String title,body,date,fragment;
    Database db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //Toast.makeText(getApplicationContext(),R.string.title_home,Toast.LENGTH_LONG).show();
                    selectedFragment = HomeFragment.newInstance();
                    break;
                case R.id.navigation_cart:
                    selectedFragment = CartFragment.newInstance();
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = NotificationFragment.newInstance();
                    break;
                case R.id.navigation_categories:
                    selectedFragment = OfferFragment.newInstance();
                    break;

            }
            openFragment(selectedFragment);
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            //transaction.replace(R.id.frame_layout, selectedFragment);
            //transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
        //openFragment(HomeFragment.newInstance());
        startgetService();
        db=new Database(this);
        getdata();
        if(GetStrings.notificationMsg){
            navigation.getMenu().findItem(R.id.navigation_notifications).setChecked(true);
            openFragment(NotificationFragment.newInstance());
            GetStrings.notificationMsg=false;
        }else{
            if(fragment!=null && fragment.equals("CartFragment")){
                navigation.getMenu().findItem(R.id.navigation_cart).setChecked(true);
                openFragment(CartFragment.newInstance());
            }else{
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                openFragment(HomeFragment.newInstance());
            }

        }
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
       // transaction.addToBackStack(null);
        transaction.commit();
    }

    private void startgetService() {
        try {
            //todo handle no internet connect with other way
            if (!GetUtilities.IsMyServiceRunning(this, getService.class)) {
                Intent i = new Intent(this, getService.class);
                startService(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getdata() {
        try {
            title= getIntent().getStringExtra("title");
            //Toast.makeText(getBaseContext(),title,Toast.LENGTH_LONG).show();
            body= getIntent().getStringExtra("body");
            //Toast.makeText(getBaseContext(),body,Toast.LENGTH_LONG).show();
            date= getIntent().getStringExtra("date");
            //Toast.makeText(getBaseContext(),date,Toast.LENGTH_LONG).show();
            fragment= getIntent().getStringExtra("fragment");
            /*if(title!=null && body!=null && date!=null) {
                db.addNotification(title, body, date);
                GetStrings.notificationMsg=true;
                db.close();
            }*/
        }catch (Exception e){
            return;
        }
    }

}

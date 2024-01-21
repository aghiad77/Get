package com.example.aghiad_pc.get.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aghiad_pc.get.Behaviour.getService;
import com.example.aghiad_pc.get.Model.Database;
import com.example.aghiad_pc.get.R;
import com.example.aghiad_pc.get.Utilities.GetStrings;
import com.example.aghiad_pc.get.Utilities.GetUtilities;

public class mainSplashActivity extends AppCompatActivity {

    Database db;
    RelativeLayout get;
    //TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splash);
        startgetService();
        //animationImage();
        db=new Database(this);
        //db.onUpgrade(db.getWritableDatabase(),0,1);
        Class SelectedClass;
        if(db.getIsUserLogged())
            SelectedClass = HomeActivity.class;
        else
            SelectedClass = LoginActivity.class;

        final Class passInClass = SelectedClass;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(mainSplashActivity.this, passInClass);
                startActivity(i);
                finish();
            }
        }, GetStrings.SPLASH_TIMEOUT);
    }

    private void startgetService() {
        try {
            //todo handle no internet connect with other way
            if (!GetUtilities.IsMyServiceRunning(this, getService.class)) {
                Intent i = new Intent(mainSplashActivity.this, getService.class);
                startService(i);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void animationImage() {
        get=(RelativeLayout) findViewById(R.id.get);
        //text=(TextView) findViewById(R.id.text);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.side_slide);
        get.setAnimation(slideAnimation);
        slideAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                 //text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}

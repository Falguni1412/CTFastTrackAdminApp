package com.example.ctfasttrackadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 5000;
    //    Initialize the objects
    ImageView splash_app_logo;
    TextView splash_app_title;

    Animation top_to_bottom, bottom_to_top;
    ActivityOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        //find the ids
        splash_app_logo = findViewById(R.id.img_splash_logo);
        splash_app_title = findViewById(R.id.tv_splash_title);

        top_to_bottom = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom_anim);
        bottom_to_top = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top_anim);

        splash_app_logo.setAnimation(top_to_bottom);
        splash_app_title.setAnimation(bottom_to_top);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View, String>(splash_app_logo, "splash_logo");
//                    pairs[0] = new Pair<View,String>(splash_app_title,"splash_title");


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs[0]);
                }
                startActivity(intent, options.toBundle());

            }
        }, SPLASH_SCREEN);
    }
}
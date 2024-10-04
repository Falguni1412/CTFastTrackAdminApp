package com.example.ctfasttrackadminapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.example.ctfasttrackadminapp.AddBus.AddBusActivity;
import com.example.ctfasttrackadminapp.ViewBooking.ViewBookingActivity;

public class HomeActivity extends AppCompatActivity {

    CardView cardView11,cardView22,cardView33,cardView44,cardView55,cardView66;
    boolean doubletap = false;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Admin App");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        boolean firsttime = sharedPreferences.getBoolean("firsttime",true);

        if (firsttime)
        {
            welcome();
        }

        cardView11 = findViewById(R.id.cardview11);
        cardView22 = findViewById(R.id.cardview22);
//        cardView33 = findViewById(R.id.cardview33);
//        cardView44 = findViewById(R.id.cardview44);
        cardView55 = findViewById(R.id.cardview55);
        cardView66 = findViewById(R.id.cardview66);

        cardView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddBusActivity.class));
            }
        });

        cardView22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this, ViewBookingActivity.class));

            }
        });

//        cardView33.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, ViewAllUsersActivity.class));
//
//            }
//        });
//
//        cardView44.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, ViewAllBookingActivity.class));
//
//            }
//        });
//
//        cardView55.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, ViewAll.class));
//
//            }
//        });

        cardView66.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });

    }

    private void welcome() {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Admin App");
        ad.setMessage("Welcome to Bus Admin App");
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).create().show();

        SharedPreferences sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firsttime",false);
        editor.apply();
    }

    public void logout()
    {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Logout");
        ad.setMessage("Are you sure for logout");
        ad.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ad.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putBoolean("isLogin", false).commit();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));

            }
        }).create().show();
    }

    @Override
    public void onBackPressed() {
        if (doubletap)
        {
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this, "Please Press again to exit the app", Toast.LENGTH_SHORT).show();
            doubletap = true;
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubletap = false;
                }
            },2000);
        }
    }
}
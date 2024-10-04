package com.example.ctfasttrackadminapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ctfasttrackadminapp.Comman.Urls;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 0000;
    TextInputEditText tie_login_username,tie_login_password;
    ImageView img_login_logo;
    Button btn_login_login;
    ActivityOptions options;
    ProgressDialog progressDialog;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login Page");
        getSupportActionBar().hide();

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor = preferences.edit();

        if (preferences.getBoolean("isLogin",false))
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        img_login_logo = findViewById(R.id.img_login_logo);
        tie_login_username = findViewById(R.id.tie_login_username);
        tie_login_password = findViewById(R.id.tie_login_password);
        btn_login_login = findViewById(R.id.btn_login_login);

        btn_login_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Login User");
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                loginUser();
            }
        });

    }

    private void loginUser() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",tie_login_username.getText().toString());
        params.put("password",tie_login_password.getText().toString());

        client.post(Urls.urlLoginAdmin,params,new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                progressDialog.show();
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
               progressDialog.hide();

                try {
                    String aa = response.getString("success");
                    if (aa.equals("1"))
                    {
//                        Toast.makeText(LoginActivity.this, ""+id+""+category_type+""+restaurant_name, Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isLogin", true).commit();
                        editor.putString("username",tie_login_username.getText().toString()).commit();

//                        Toast.makeText(LoginActivity.this, "Login Successfully Done", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(LoginActivity.this, "Could Not Connect", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
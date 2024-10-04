package com.example.ctfasttrackadminapp.ViewBooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctfasttrackadminapp.Comman.Urls;
import com.example.ctfasttrackadminapp.HomeActivity;
import com.example.ctfasttrackadminapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ViewBookingDetailsActivity extends AppCompatActivity {

    ImageView img_bus_profilel;
    TextView tv_id,tv_bus_id,tv_bus_number,tv_bus_from,tv_bus_to,tv_date,tv_time,tv_user_name,tv_user_mobile_no,
    tv_user_address;

    Button btn_cancel_booking;
    ProgressBar progress;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    String id,bus_id, bus_number, bus_from, bus_to, date, time, user_name, user_mobile_no, user_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_details);
        preferences = PreferenceManager.getDefaultSharedPreferences(ViewBookingDetailsActivity.this);
        editor = preferences.edit();

        img_bus_profilel = findViewById(R.id.img_view_booking_in_details_profile);
        tv_bus_id = findViewById(R.id.tv_view_booking_in_details_bus_id);
        tv_bus_number = findViewById(R.id.tv_view_booking_in_details_bus_number);
        tv_bus_from = findViewById(R.id.tv_view_booking_in_details_bus_from);
        tv_bus_to = findViewById(R.id.tv_view_booking_in_details_bus_to);
        tv_date = findViewById(R.id.tv_view_booking_in_detail_date);
        tv_time = findViewById(R.id.tv_view_booking_in_details_time);
        tv_user_name = findViewById(R.id.tv_view_booking_in_details_user_name);
        tv_user_mobile_no = findViewById(R.id.tv_view_booking_in_details_user_mobile_no);
        tv_user_address = findViewById(R.id.tv_view_booking_in_details_user_address);

        btn_cancel_booking = findViewById(R.id.btn_view_booking_in_details_cancel_booking);
        progress = findViewById(R.id.progress);

        id = preferences.getString("id", "");
        bus_id = preferences.getString("bus_id", "");
        bus_number = preferences.getString("bus_number", "");
        bus_from = preferences.getString("bus_from", "");
        bus_to = preferences.getString("bus_to", "");
        date = preferences.getString("date", "");
        time = preferences.getString("time", "");
        user_name = preferences.getString("user_name", "");
        user_mobile_no = preferences.getString("user_mobile_no", "");
        user_address = preferences.getString("user_address", "");

//        Picasso.with(ViewBookingDetailsActivity.this).load(Urls.OnlineImageAddress + "" + image).placeholder(R.drawable.profileimage)
//                .error(R.drawable.image_not_load).into(img_hostel_profilel);

        setTitle("" + bus_number);

        tv_bus_id.setText("Bus No: "+bus_id);
        tv_bus_number.setText(bus_number);
        tv_bus_from.setText("Bus From: "+bus_from);
        tv_bus_to.setText("Bus To: "+bus_to);
        tv_date.setText("Date: "+date);
        tv_time.setText("Time: "+time);
        tv_user_name.setText("User Name: "+user_name);
        tv_user_mobile_no.setText("User Mobile No: "+user_mobile_no);
        tv_user_address.setText("User Address: "+user_address);

        btn_cancel_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(ViewBookingDetailsActivity.this);
                ad.setTitle("")
                        .setMessage("Are You Sure You Want To Delete")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.setVisibility(View.VISIBLE);
                                        deleteBooking(id);
                                    }
                                },2000);

                            }
                        });

                AlertDialog alertDialog = ad.create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.darker_gray);

            }
        });
    }

    private void deleteBooking(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id","5");
        client.post(Urls.urlDeleteBooking, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String aa = response.getString("success");

                    if (aa.equals("1")) {
                        progress.setVisibility(View.GONE);
                        Intent intent = new Intent(ViewBookingDetailsActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        progress.setVisibility(View.GONE);
                        Toast.makeText(ViewBookingDetailsActivity.this, "Unable to delete Booking of Hostel", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                 progress.setVisibility(View.GONE);
                Toast.makeText(ViewBookingDetailsActivity.this, "Could Not Connect", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
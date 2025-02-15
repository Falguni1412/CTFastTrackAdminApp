package com.example.ctfasttrackadminapp.ViewBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctfasttrackadminapp.Comman.Urls;
import com.example.ctfasttrackadminapp.HomeActivity;
import com.example.ctfasttrackadminapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ViewBookingActivity extends AppCompatActivity {

    List<POJOViewAllBooking> list;
    ListView lv_all_buses;
    TextView tv_no_records;
    ProgressBar pBar;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ViewAllBookingAdapter adapter;

    SearchView searchView_bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking);

        preferences= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor=preferences.edit();

        setTitle("Booking List");

        searchView_bus= findViewById(R.id.searchview_busbysourcedestination);
        list = new ArrayList<POJOViewAllBooking>();
        lv_all_buses = (ListView) findViewById(R.id.lv_all_buses);
        tv_no_records = (TextView) findViewById(R.id.tv_no_records);

        pBar = (ProgressBar) findViewById(R.id.pBar);

        searchView_bus.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchbus(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchbus(newText);
                return false;
            }
        });


        getAllBuses();

    }//oncreate

    private void searchbus(String query) {

        List<POJOViewAllBooking> tempcenterlist = new ArrayList();
        tempcenterlist.clear();

        for (POJOViewAllBooking d : list) {
            if (d.getBus_from().toUpperCase().contains(query.toUpperCase()) || d.getBus_to().toUpperCase().contains(query
                    .toUpperCase()) || d.getUser_name().toUpperCase().contains(query
                    .toUpperCase()))
                tempcenterlist.add(d);
        }

        adapter = new ViewAllBookingAdapter(ViewBookingActivity.this,tempcenterlist ,tv_no_records);
        lv_all_buses.setAdapter(adapter);
    }

    private void getAllBuses() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.url_all_booking, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                pBar.setVisibility(View.VISIBLE);
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pBar.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = response.getJSONArray("getAllBooking");
                    if (jsonArray.isNull(0)) {
                        tv_no_records.setVisibility(View.VISIBLE);
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String bus_id = jsonObject.getString("bus_id");
                        String bus_number = jsonObject.getString("bus_number");
                        String bus_from = jsonObject.getString("bus_from");
                        String bus_to = jsonObject.getString("bus_to");
                        String date = jsonObject.getString("date");
                        String time = jsonObject.getString("time");
                        String name = jsonObject.getString("name");
                        String contact = jsonObject.getString("contact");
                        String address = jsonObject.getString("address");

                        list.add(new POJOViewAllBooking(id, bus_id,bus_number, bus_from, bus_to, date, time,
                                name, contact, address ));
                    }
                    adapter = new ViewAllBookingAdapter(ViewBookingActivity.this, list, tv_no_records);
                    lv_all_buses.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ViewBookingActivity.this, "Could Not Connect", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(ViewBookingActivity.this, HomeActivity.class));
        finish();
    }

}
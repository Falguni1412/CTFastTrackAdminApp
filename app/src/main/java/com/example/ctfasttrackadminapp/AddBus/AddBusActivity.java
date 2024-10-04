package com.example.ctfasttrackadminapp.AddBus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.ctfasttrackadminapp.Comman.Urls;
import com.example.ctfasttrackadminapp.Comman.VolleyMultipartRequest;
import com.example.ctfasttrackadminapp.HomeActivity;
import com.example.ctfasttrackadminapp.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AddBusActivity extends AppCompatActivity {

    EditText et_bus_from,et_bus_to,et_bus_number,et_total_seats,et_date,et_time;
    Button btn_add_bus_image,btn_add_bus;
    ImageView img_bus;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    ProgressBar progress;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        getSupportActionBar().hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        progress = findViewById(R.id.progress);

        img_bus = findViewById(R.id.img_bus_image);
        et_bus_from = findViewById(R.id.edt_from_station);
        et_bus_to = findViewById(R.id.edt_to_station);
        et_bus_number = findViewById(R.id.edt_number);
        et_total_seats = findViewById(R.id.edt_bus_seats);
        et_date = findViewById(R.id.edt_bus_date);
        et_time = findViewById(R.id.edt_bus_time);
        btn_add_bus_image = findViewById(R.id.btn_choose_bus_image);
        btn_add_bus = findViewById(R.id.btn_add_bus);
//        btn_add_restaurant_address = findViewById(R.id.btn_add_restaurant_addres);
//        txt_show_selected_restaurant_addres = findViewById(R.id.edt_show_restaurant_address);

        btn_add_bus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btn_add_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_bus_from.getText().toString().isEmpty()) {
                    et_bus_from.setError("Please Enter Bus From");
                }
                else if (et_bus_to.getText().toString().isEmpty()) {
                    et_bus_to.setError("Please Enter Bus To");
                }
                else if (et_bus_number.getText().toString().isEmpty()) {
                    et_bus_number.setError("Please Enter Bus Number");
                }
                else if (et_total_seats.getText().toString().isEmpty()) {
                    et_total_seats.setError("Please Enter Bus Total Seats");
                }
                else if (et_date.getText().toString().isEmpty()) {
                    et_date.setError("Please Enter Bus Date");
                }
                else if (et_time.getText().toString().isEmpty()) {
                    et_time.setError("Please Enter Bus Time");
                }
                else {
                    addBus();
                }
            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img_bus.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addBus() {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("bus_from", et_bus_from.getText().toString());
        params.put("bus_to", et_bus_to.getText().toString());
        params.put("bus_number", et_bus_number.getText().toString());
        params.put("total_seats", et_total_seats.getText().toString());
        params.put("date", et_date.getText().toString());
        params.put("time", et_time.getText().toString());

        client.post(Urls.urlAddBus, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                progress.setVisibility(View.VISIBLE);
                super.onStart();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progress.setVisibility(View.GONE);
                try {
                    String aa = response.getString("success");

                    if (aa.equals("1")) {
                        uploadBitmap(bitmap, response.getInt("lastinsertedid"));
                    } else {
                        Toast.makeText(AddBusActivity.this, "Unable to Add Bus", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(AddBusActivity.this, "Could not Connect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadBitmap(final Bitmap bitmap, final int lastinsertedid) {
        //getting the tag from the edittext
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, Urls.urlAddBusImage,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(AddBusActivity.this, "Bus Added Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddBusActivity.this, HomeActivity.class));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("tags", "" + lastinsertedid);
                return params;
            }

            /*
             * Here we are passing image by renaming it with a unique name
             * */
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}
package com.example.yourweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;


import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private EditText editSearch;
    private Button btnSearch, btnChangeActivity, bt_gps;
    private TextView txtName, txtCountry, txtTemp, txtStatus,
            txtHumidity, txtCloud, txtWind, txtDay,txtgrnd_level;
    private ImageView imgIcon;

    private FusedLocationProviderClient client;
    String latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapping();
        search();
        requestPermission();
        gpsSeach();
        getLonLat();


        client = LocationServices.getFusedLocationProviderClient(this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lon = location.getLongitude();

                            latitude = String.valueOf(lat);
                            longitude = String.valueOf(lon);

                            GetGPSCurrentWeather();
                        }
                    }
                });


    }
    private void mapping() {
        editSearch = findViewById(R.id.edittextSearch);
        btnSearch = findViewById(R.id.buttonSearch);
        btnChangeActivity = findViewById(R.id.buttonChangeActivity);
        txtName = findViewById(R.id.textviewName);
        txtCountry = findViewById(R.id.textViewCountry);
        txtTemp = findViewById(R.id.textviewTemp);
        txtStatus = findViewById(R.id.textviewStatus);
        txtHumidity = findViewById(R.id.textviewhumidity);
        txtCloud = findViewById(R.id.textviewCloud);
        txtWind = findViewById(R.id.textviewWind);
        txtDay = findViewById(R.id.textviewDay);
        imgIcon = findViewById(R.id.imageIcon);
        txtgrnd_level = findViewById(R.id.txtgrnd);

        // get lon lat 3g or wifi.....................
        bt_gps = findViewById(R.id.getLocation);

    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }
    public void getLonLat(){
        client = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();

                    latitude = String.valueOf(lat);
                    longitude = String.valueOf(lon);
                }
            }
        });
    }
    public void gpsSeach(){
        bt_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLonLat();
                GetGPSCurrentWeather();

            }
        });
    }
    private void search() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                GetCurrentWeather(city);
            }
        });
    }
    public void GetCurrentWeather(String data) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + data + "&units=metric&appid=fd6c4d0476b4fd11ea44e47d7a7fb4af";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText(name);

                            long l = Long.valueOf(day);
                            java.util.Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy/MM/dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                            String status = jsonObjectweather.getString("main");
                            String icon = jsonObjectweather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            String apxuat = jsonObjectMain.getString("pressure");

                            Double chuyenkiu = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(chuyenkiu.intValue());
                            txtTemp.setText(Nhietdo+"°C");
                            txtHumidity.setText(String.format("%s %%", doam));
                            txtgrnd_level.setText(apxuat+" hPa");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+" m/s");


                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectCloud.getString("all");
                            txtCloud.setText(may+" %");

                            JSONObject jsonObjectSYS = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSYS.getString("country");
                            txtCountry.setText(country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
    public void GetGPSCurrentWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&units=metric&appid=fd6c4d0476b4fd11ea44e47d7a7fb4af\n";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText(name);

                            long l = Long.valueOf(day);
                            java.util.Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy/MM/dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                            String status = jsonObjectweather.getString("main");
                            String icon = jsonObjectweather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            String apxuat = jsonObjectMain.getString("pressure");

                            Double chuyenkiu = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(chuyenkiu.intValue());
                            txtTemp.setText(Nhietdo+"°C");
                            txtHumidity.setText(String.format("%s %%", doam));
                            txtgrnd_level.setText(apxuat+" hPa");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+" m/s");


                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectCloud.getString("all");
                            txtCloud.setText(may+" %");

                            JSONObject jsonObjectSYS = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSYS.getString("country");
                            txtCountry.setText(country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }



    public void GetGPS5DayWeather() {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "http://http://api.openweathermap.org/data/2.5/find?lat="+latitude+"&lon="+longitude+"&cnt=5&appid=fd6c4d0476b4fd11ea44e47d7a7fb4af\n";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText(name);

                            long l = Long.valueOf(day);
                            java.util.Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy/MM/dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectweather = jsonArray.getJSONObject(0);
                            String status = jsonObjectweather.getString("main");
                            String icon = jsonObjectweather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");
                            String apxuat = jsonObjectMain.getString("pressure");

                            Double chuyenkiu = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(chuyenkiu.intValue());
                            txtTemp.setText(Nhietdo+"°C");
                            txtHumidity.setText(String.format("%s %%", doam));
                            txtgrnd_level.setText(apxuat+" hPa");

                            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
                            String gio = jsonObjectWind.getString("speed");
                            txtWind.setText(gio+" m/s");


                            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectCloud.getString("all");
                            txtCloud.setText(may+" %");

                            JSONObject jsonObjectSYS = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSYS.getString("country");
                            txtCountry.setText(country);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }


}





package com.bluegoober.weatherapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;



public class MainActivity extends AppCompatActivity {

    public final static String APIKEY_CLIMACELL = BuildConfig.CLIMAPIKEY;
    public final static String BASE_URL_CLIMACELL = "https://api.climacell.co/v3/weather/";
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    String userLat = "";
    String userLongi = "";
    TextView tempTxt, tempMinMax, windTxt, lastUpdatedView;
    ArrayList<HourlyWeather> hourlyWeatherList = new ArrayList<>();
    ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();
    LinearLayoutManager hourlyLinearLayout, dailyLinearLayout;
    HourlyWeatherAdapter hourlyWeatherAdapter;
    DailyWeatherAdapter dailyWeatherAdapter;
    RelativeLayout cardView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the LocationManager and LocationListener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            //When the location is changes, update the userLat and userLongi and call the updateWeather method
            @Override
            public void onLocationChanged(@NonNull Location location) {
                userLat = location.getLatitude() + "";
                userLongi = location.getLongitude() + "";
                updateWeather();
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        //Instantiate views
        tempTxt = findViewById(R.id.current_temp_view);
        tempMinMax = findViewById(R.id.hi_lo_view);
        windTxt = findViewById(R.id.wind_view);
        lastUpdatedView = findViewById(R.id.last_updated_view);
        cardView = findViewById(R.id.current_condition_card);
        progressBar = findViewById(R.id.loading_spinner);



        //Instantiate RecyclerViews
        RecyclerView hourlyRV = findViewById(R.id.hourly_recycler);
        RecyclerView dailyRV = findViewById(R.id.daily_recycler);

        //Set RecyclerView Adapters and Layout managers
        hourlyWeatherAdapter = new HourlyWeatherAdapter(hourlyWeatherList);
        dailyWeatherAdapter = new DailyWeatherAdapter(dailyWeatherList);
        hourlyLinearLayout = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        dailyLinearLayout = new LinearLayoutManager(this);
        hourlyRV.setLayoutManager(hourlyLinearLayout);
        hourlyRV.setAdapter(hourlyWeatherAdapter);
        dailyRV.setLayoutManager(dailyLinearLayout);
        dailyRV.setAdapter(dailyWeatherAdapter);

        //Create new databasehelper
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        //Get the current weather
        CurrentWeather currentWeather = db.getCurrent();
        //If current weather data is not null
        if(currentWeather != null) {
            //Get the time of the last weather update and convert it milliseconds
            String updatedString = currentWeather.getTimeUpdated();
            long currentTimeMillis = System.currentTimeMillis();
            long lastUpdatedMillis = Instant.parse(updatedString).toEpochMilli();
            //Calculate the time since the last weather update
            long timeSinceUpdateMillis = currentTimeMillis - lastUpdatedMillis;

            //If it has been more than one hour since the last weather update, retrieve new weather data
            if (timeSinceUpdateMillis > 3600000) {
                onRetrieveLocation();
            }
            //Set the weather data
            setWeatherData();
        }
        //If current weather is null, retrieve the weather data
        if(currentWeather == null) {
            onRetrieveLocation();
        }
        db.close();
    }

    //Inflate the ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Set the click options for the menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                onRetrieveLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Method to retrieve the users location
    void onRetrieveLocation() {
        //Check if the permissions are enabled and request them if they are not
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }

        //Set progress bar to visible
        progressBar.setVisibility(View.VISIBLE);
        //Request a single location update from the GPS
        locationManager.requestSingleUpdate("gps",  locationListener, null);
    }

    //Method to set the UI weather data
    public void setWeatherData() {
        //Create new instance of DatabaseHelper
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        if(db.getCurrent() != null) {

            //Clear the weather data lists to prepare them for the new data
            hourlyWeatherList.clear();
            dailyWeatherList.clear();

            //get ArrayList of HourlyWeather objects from the database
            ArrayList<HourlyWeather> hourlyWeatherArrayList = db.getAllHourly();
            //For all of the HourlyWeather objects in the list add them to the hourlyWeatherList
            for (int i = 0; i < hourlyWeatherArrayList.size(); i++) {
                hourlyWeatherList.add(hourlyWeatherArrayList.get(i));
            }
            //Notify the hourly RecyclerView adapter that the dataset has changed.
            hourlyWeatherAdapter.notifyDataSetChanged();

            //Get arraylist of DailyWeather objects from the database
            ArrayList<DailyWeather> dailyWeatherArrayList = db.getAllDaily();
            //For all of the objects in the array add them to the dailyWeatherList
            for (int i = 0; i < dailyWeatherArrayList.size(); i++) {
                dailyWeatherList.add(dailyWeatherArrayList.get(i));
            }
            //Notify the RecyclerView adapter that the dataset has changed
            dailyWeatherAdapter.notifyDataSetChanged();

            //Get the current weather from the database
            CurrentWeather testWeather = db.getCurrent();

            //Get the local device timezone and format the date strings
            Calendar cal = Calendar.getInstance();
            TimeZone tz = cal.getTimeZone();
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
            String time = testWeather.getTimeUpdated();
            LocalDateTime timeFormat = LocalDateTime.parse(time, inputFormatter);
            String localFormattedTime = timeFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("h:mm a z").withZone(ZoneId.of(tz.toZoneId().toString())));

            lastUpdatedView.setText(localFormattedTime);
            String temperatureString = testWeather.getCurrentTemp() + "F";
            tempTxt.setText(temperatureString);
            String windString = testWeather.getWindSpeed() + " mph";
            windTxt.setText(windString);
            String feelsLikeString = testWeather.getFeelsLikeTemp() + "F";
            tempMinMax.setText(feelsLikeString);
            db.close();
        }
        else {
            Toast toast = Toast.makeText(this, "There was an issue updating the weather", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    //Method to update the weather data
    public void updateWeather() {
        //If the user location is not set
        if(userLat.equals("") || userLongi.equals("")) {

            //Tell the user there was an issue retrieving the location
            progressBar.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(MainActivity.this, "Error retrieving location", Toast.LENGTH_LONG);
            toast.show();

        }
        //If the user location is not empty
        if(!userLat.equals("") && !userLongi.equals("")) {
            //Create long for the time now plus 20 hours for the hourly forecast
            long hourDateInMillis = System.currentTimeMillis() + 86400000;
            //Create long time plus 8 days for the daily forecast
            long eightDayInMillis = System.currentTimeMillis() + 691200000;
            //String to format the millisecond dates and create new date formatter
            String format = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
            final SimpleDateFormat sdf = new SimpleDateFormat(format);
            Log.d("Long and Lat", userLat + " " + userLongi);

            //Format the date strings for use in the API call
            String hourDateString = sdf.format(new Date(hourDateInMillis));
            String eightDayString = sdf.format(new Date(eightDayInMillis));

            //Create new instance of WeatherDataHelper
            WeatherDataHelper weatherDataHelper = new WeatherDataHelper();

            //Call the methods in WeatherDataHelper to update the current, hourly, and daily weather data
            weatherDataHelper.requestDataCurrent(this, BASE_URL_CLIMACELL + "realtime" + "?lat=" + userLat + "&lon=" + userLongi +
                    "&unit_system=us&fields=temp%2Cfeels_like%2Cwind_speed&apikey=" + APIKEY_CLIMACELL);
            weatherDataHelper.requestDataHourly(this, BASE_URL_CLIMACELL + "forecast/hourly" + "?lat=" + userLat + "&lon=" + userLongi +
                    "&unit_system=us&start_time=now&end_time=" + hourDateString + "&fields=temp%2Cwind_speed%2Cprecipitation_probability&apikey=" + APIKEY_CLIMACELL);
            weatherDataHelper.requestDataEightDay(this, BASE_URL_CLIMACELL + "forecast/daily" + "?lat=" + userLat + "&lon=" + userLongi +
                    "&unit_system=us&start_time=now&end_time=" + eightDayString + "&fields=temp%2Cwind_speed%2Cprecipitation_probability%2Chumidity%2Csunrise%2Csunset%2Cmoon_phase%2Cweather_code&apikey=" + APIKEY_CLIMACELL);

            //Call the setWeatherData method after waiting 3500ms for the API to respond
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setWeatherData();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }, 3500);


        }


    }

}
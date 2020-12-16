package com.bluegoober.weatherapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DetailedWeatherActivity extends AppCompatActivity {

    public static final String DETAILED_COUNTDOWN_ID = "countdown_id";
    TextView sunriseView, sunsetView, moonView, windHigh, windLow, precipChanceView, tempHighView, tempLowView, dateView;
    ImageView conditionIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_weather);

        //Instantiate the ActionBar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button if the ActionBar is not null
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }


        dateView = findViewById(R.id.detailed_date_view);
        tempHighView = findViewById(R.id.detailed_hightemp_view);
        tempLowView = findViewById(R.id.detailed_lowtemp_view);
        sunriseView = findViewById(R.id.detailed_sunrise_view);
        sunsetView = findViewById(R.id.detailed_sunset_view);
        moonView = findViewById(R.id.detailed_moon_view);
        windHigh = findViewById(R.id.detailed_high_wind_view);
        windLow = findViewById(R.id.detailed_low_wind_view);
        precipChanceView = findViewById(R.id.detailed_precip_view);
        conditionIcon = findViewById(R.id.conditions_icon);

        //Get intents
        Intent intent = getIntent();

        //Get extras from the intent
        Bundle extras = intent.getExtras();

        //If extras exist, retrieve the weather data for the provided day ID
        if(extras != null) {
            int countdownID = getIntent().getExtras().getInt(DETAILED_COUNTDOWN_ID);
            setWeatherData(countdownID);
        }

    }

    //Method to set the weather data
    public void setWeatherData(int id) {
        //Get new instance of the database and retrieve the specified day from the database
        DatabaseHelper databaseHelper = new DatabaseHelper(DetailedWeatherActivity.this);
        DailyWeather dailyWeather = databaseHelper.getDaily(id);

        int iconID = dailyWeather.getConditionIcon();
        conditionIcon.setImageResource(iconID);

        //Retrieve the weather fields and create the formatted strings for them
        int maxTemperature = dailyWeather.getMaxTemp();
        String maxTempString = maxTemperature + dailyWeather.getMaxTempUnit();
        int minTemperature = dailyWeather.getMinTemp();
        String minTempString = minTemperature + dailyWeather.getMinTempUnit();
        int windSpeedHigh = dailyWeather.getMaxWind();
        String windHighString = windSpeedHigh + " " + dailyWeather.getMaxWindUnit();
        int windSpeedLow = dailyWeather.getMinWind();
        String windLowString = windSpeedLow + " " + dailyWeather.getMinWindUnit();
        int precipChance = dailyWeather.getPrecipChance();
        String precipString = precipChance + dailyWeather.getPrecipUnit();


        //Set views to display weather data
        tempHighView.setText(maxTempString);
        tempLowView.setText(minTempString);
        precipChanceView.setText(precipString);
        windHigh.setText(windHighString);
        windLow.setText(windLowString);

        String sunsetTime = dailyWeather.getSunsetTime();
        String sunriseTime = dailyWeather.getSunriseTime();
        String date = dailyWeather.getDate();
        //Format the moon phase string to remove any underscores and capitalize the first letter
        String moonPhase = dailyWeather.getMoonPhase().substring(0,1).toUpperCase() + dailyWeather.getMoonPhase().replace('_',' ').substring(1);


        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        LocalDateTime sunsetFormat = LocalDateTime.parse(sunsetTime, inputFormatter);
        LocalDateTime sunriseFormat = LocalDateTime.parse(sunriseTime, inputFormatter);
        String actualLocalSunset = sunsetFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("h:mm a").withZone(tz.toZoneId()));
        String actualLocalSunrise = sunriseFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("h:mm a").withZone(tz.toZoneId()));

        DateTimeFormatter inputFormatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate dateFormat = LocalDate.parse(date, inputFormatterDate);
        String formattedDate = dateFormat.format(DateTimeFormatter.ofPattern("EEEE, MMMM d"));

        moonView.setText(moonPhase);
        sunriseView.setText(actualLocalSunrise);
        sunsetView.setText(actualLocalSunset);
        dateView.setText(formattedDate);
        //Set the ActionBar title to the date
        Objects.requireNonNull(getSupportActionBar()).setTitle(formattedDate);

    }
}
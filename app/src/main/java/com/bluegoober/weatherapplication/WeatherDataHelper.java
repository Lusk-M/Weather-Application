package com.bluegoober.weatherapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;

public class WeatherDataHelper {

    //Asynchronously get the current weather data from ClimaCell
    public void requestDataCurrent(final Context context, String url) {
        //Create a new instance of AsyncClient
        AsyncHttpClient client = new AsyncHttpClient();

        //Get the jSon data from the URL
        client.get(url, new JsonHttpResponseHandler() {
            //If jSon data was successfully pulled
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Weather", "JSON: " + response.toString());

                //Create instance of calendar and timezone to be used in date and time conversion
                Calendar cal = Calendar.getInstance();
                TimeZone tz = cal.getTimeZone();
                //DateFormatter to parse the date from ClimaCell
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

                try {
                    JSONObject currentWeather = response;

                    //Retrieve the current temperature
                    JSONObject temp = currentWeather.getJSONObject("temp");
                    int tempValue = (int)Math.round(temp.getDouble("value"));
                    String tempUnit = temp.getString("units");

                    //Retrieve the current feel-like temp
                    JSONObject feelsLike = currentWeather.getJSONObject("feels_like");
                    int feelsLikeTemp = feelsLike.getInt("value");
                    String feelsLikeUnit = feelsLike.getString("units");

                    //Get the current wind speed
                    JSONObject wind = currentWeather.getJSONObject("wind_speed");
                    int windSpeed = Math.round(wind.getInt("value"));
                    String windUnit = wind.getString("units");

                    //Get the observation time and convert it to local timezone
                    JSONObject observationTime = currentWeather.getJSONObject("observation_time");
                    String time = observationTime.getString("value");
                    LocalDateTime timeFormat = LocalDateTime.parse(time, inputFormatter);
                    String timeString = timeFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mm a").withZone(tz.toZoneId()));

                    //create a currentWeatherObject with the JSON data
                    CurrentWeather currentWeatherObject = new CurrentWeather(time, tempValue, feelsLikeTemp, windSpeed);
                    //Create instance of databasehelper
                    DatabaseHelper db = new DatabaseHelper(context);

                    //If the record exists in the database update it, else create a new record
                    if(db.getCurrent() != null) {
                        db.updateCurrent(currentWeatherObject);
                    }
                    else {
                        db.createCurrent(currentWeatherObject);
                    }
                    db.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {

                Toast.makeText(context, "Request Failed Current",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDataHourly(final Context context, String url) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
                // called when response HTTP status is "200 OK"
                Log.d("Weather", "JSON: " + response.toString());

                try {
                    Calendar cal = Calendar.getInstance();
                    TimeZone tz = cal.getTimeZone();
                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                    for(int i = 0; i < response.length() ; i++) {
                        JSONObject hour = response.getJSONObject(i);

                        JSONObject temp = hour.getJSONObject("temp");
                        int tempValue = (int)Math.round(temp.getDouble("value"));
                        String tempUnit = temp.getString("units");

                        JSONObject precip = hour.getJSONObject("precipitation_probability");
                        int precipChance = precip.getInt("value");
                        String precipUnit = precip.getString("units");

                        JSONObject wind = hour.getJSONObject("wind_speed");
                        int windSpeed = Math.round(wind.getInt("value"));
                        String windUnit = wind.getString("units");

                        JSONObject observationTime = hour.getJSONObject("observation_time");
                        String time = observationTime.getString("value");
                        LocalDateTime timeFormat = LocalDateTime.parse(time, inputFormatter);
                        String timeString = timeFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("hh:mm a").withZone(tz.toZoneId()));

                        HourlyWeather hourlyObject = new HourlyWeather(time, tempValue, precipChance, windSpeed);

                        DatabaseHelper db = new DatabaseHelper(context);
                        if(db.getHourly(i+1) != null) {
                            db.updateHourly(hourlyObject, i+1);
                        }
                        else {
                            db.createHourly(hourlyObject);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {

                Toast.makeText(context, "Request Failed Hourly",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestDataEightDay(final Context context, final String url) {
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
                // called when response HTTP status is "200 OK"
                Log.d("Weather", "JSON: " + response.toString());


                try {
                    JSONObject day1 = response.getJSONObject(0);

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject day = response.getJSONObject(i);

                        //Get the temperature
                        JSONArray temp = day.getJSONArray("temp");
                        //get min temp
                        JSONObject minTempObject = temp.getJSONObject(0);
                        String timeOfMinTemp = minTempObject.getString("observation_time");
                        JSONObject minTempValues = minTempObject.getJSONObject("min");
                        int minTemp = (int) Math.round(minTempValues.getDouble("value"));
                        //Get max temp
                        JSONObject maxTempObject = temp.getJSONObject(1);
                        String timeOfMaxTemp = maxTempObject.getString("observation_time");
                        JSONObject maxTempValues = maxTempObject.getJSONObject("max");
                        int maxTemp = (int) Math.round(maxTempValues.getDouble("value"));

                        //Get the precipitation chance
                        JSONObject precip = day.getJSONObject("precipitation_probability");
                        int precipChance = precip.getInt("value");

                        //Get the humidity
                        JSONArray humidity = day.getJSONArray("humidity");
                        //get min humidity
                        JSONObject minHumidObject = humidity.getJSONObject(0);
                        String timeOfMinHumid = minHumidObject.getString("observation_time");
                        JSONObject minHumidValues = minHumidObject.getJSONObject("min");
                        int minHumid = (int) Math.round(minHumidValues.getDouble("value"));
                        //Get max humidity
                        JSONObject maxHumidObject = humidity.getJSONObject(1);
                        String timeOfMaxHumid = maxHumidObject.getString("observation_time");
                        JSONObject maxHumidValues = maxHumidObject.getJSONObject("max");
                        int maxHumid = (int) Math.round(maxHumidValues.getDouble("value"));

                        //Get the wind speed
                        JSONArray wind = day.getJSONArray("wind_speed");
                        //get min wind
                        JSONObject minWindObject = wind.getJSONObject(0);
                        String timeOfMinWind = minWindObject.getString("observation_time");
                        JSONObject minWindValues = minWindObject.getJSONObject("min");
                        int minWind = (int) Math.round(minWindValues.getDouble("value"));
                        //Get max wind
                        JSONObject maxWindObject = wind.getJSONObject(1);
                        String timeOfMaxWind = maxWindObject.getString("observation_time");
                        JSONObject maxWindValues = maxWindObject.getJSONObject("max");
                        int maxWind = (int) Math.round(maxWindValues.getDouble("value"));

                        //get sunrise
                        JSONObject sunriseTime = day.getJSONObject("sunrise");
                        String sunrise = sunriseTime.getString("value");

                        //get sunset
                        JSONObject sunsetTime = day.getJSONObject("sunset");
                        String sunset = sunsetTime.getString("value");

                        //get sunrise
                        JSONObject moonPhase = day.getJSONObject("moon_phase");
                        String moon = moonPhase.getString("value");

                        //get weather code
                        JSONObject weatherCodeObject = day.getJSONObject("weather_code");
                        String weatherCode = weatherCodeObject.getString("value");

                        //get observation time
                        JSONObject observationTime = day.getJSONObject("observation_time");
                        String date = observationTime.getString("value");

                        int conditionIconId;

                        switch (weatherCode) {
                            case "freezing_rain_heavy":
                            case "freezing_rain":
                            case "freezing_rain_light":
                            case "freezing_dribble":
                                conditionIconId = R.drawable.wi_rain_mix;
                                break;
                            case "ice_pellets_heavy":
                            case "ice_pellets":
                            case "ice_pellets_light":
                                conditionIconId = R.drawable.wi_snowflake_cold;
                                break;
                            case "snow_heavy":
                                conditionIconId = R.drawable.wi_day_snow_thunderstorm;
                                break;
                            case "snow":
                            case "snow_light":
                            case "flurries":
                                conditionIconId = R.drawable.wi_snow;
                                break;
                            case "rain_heavy":
                                conditionIconId = R.drawable.wi_raindrops;
                                break;
                            case "rain":
                            case "rain_light":
                            case "drizzle":
                                conditionIconId = R.drawable.wi_rain;
                                break;
                            case "tstorm":
                                conditionIconId = R.drawable.wi_thunderstorm;
                                break;
                            case "fog_light":
                            case "fog":
                                conditionIconId = R.drawable.wi_fog;
                                break;
                            case "cloudy":
                            case "mostly_cloudy":
                            case "partly_cloudy":
                                conditionIconId = R.drawable.wi_cloudy;
                                break;
                            case "mostly_clear":
                            case "clear":
                                conditionIconId = R.drawable.wi_day_sunny;
                                break;
                            default:
                                conditionIconId = R.drawable.wi_cloud;
                        }



                        DailyWeather dayObject = new DailyWeather(minTemp, maxTemp, minHumid, maxHumid, minWind, maxWind, precipChance, timeOfMinTemp, timeOfMaxTemp, timeOfMinHumid, timeOfMaxHumid, timeOfMinWind, timeOfMaxWind, sunrise, sunset, moon, date, conditionIconId);
                        DatabaseHelper db = new DatabaseHelper(context);
                        if (db.getDaily(i + 1) != null) {
                            db.updateDaily(dayObject, i + 1);
                        } else {
                            db.createDaily(dayObject);
                        }


                        Log.d("min temp ", minTemp + "f  max temp" + maxTemp);
                        //Log.d("Success", "True");
                    }
                    Log.d("Hour 1", day1.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e,
                                  JSONObject response) {

                Toast.makeText(context, "Request Failed Daily",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

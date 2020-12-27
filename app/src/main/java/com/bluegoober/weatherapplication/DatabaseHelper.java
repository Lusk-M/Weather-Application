package com.bluegoober.weatherapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 7;

    private static final String DATABASE_NAME = "weatherData";

    private static final String TABLE_CURRENT = "current_weather";
    private static final String TABLE_HOURLY = "hourly_weather";
    private static final String TABLE_DAILY = "daily_weather";

    private static final String ID_KEY = "id";

    //current
    private static final String CURRENT_TIME = "time_updated";
    private static final String CURRENT_TEMP ="current_temp";
    private static final String CURRENT_FEELS_LIKE = "current_feels_like";
    private static final String CURRENT_WIND_SPEED = "current_wind_speed";

    //Create current table statement
    private static final String CREATE_CURRENT_TABLE = "CREATE TABLE " + TABLE_CURRENT + "(" + ID_KEY + " INTEGER PRIMARY KEY,"
            + CURRENT_TIME + " TEXT,"
            + CURRENT_TEMP + " INTEGER,"
            + CURRENT_FEELS_LIKE + " INTEGER,"
            + CURRENT_WIND_SPEED + " INTEGER" + ")";

    //hourly
    private static final String HOURLY_TIME = "hourly_time";
    private static final String HOURLY_TEMP = "hourly_temp";
    private static final String HOURLY_WIND = "hourly_wind";
    private static final String HOURLY_PRECIP = "hourly_precip";

    //Create hourly table statement
    private static final String CREATE_HOURLY_TABLE = "CREATE TABLE " + TABLE_HOURLY + "(" + ID_KEY + " INTEGER PRIMARY KEY,"
            + HOURLY_TIME + " TEXT,"
            + HOURLY_TEMP + " INTEGER,"
            + HOURLY_WIND + " INTEGER,"
            + HOURLY_PRECIP + " INTEGER" + ")";

    //daily
    private static final String DAILY_DATE = "daily_date";
    private static final String DAILY_MAX_TEMP = "daily_max_temp";
    private static final String DAILY_TIME_MAX_TEMP = "daily_time_max_temp";
    private static final String DAILY_MIN_TEMP = "daily_min_temp";
    private static final String DAILY_TIME_MIN_TEMP = "daily_time_min_temp";
    private static final String DAILY_MAX_WIND = "daily_max_wind";
    private static final String DAILY_TIME_MAX_WIND = "daily_time_max_wind";
    private static final String DAILY_MIN_WIND = "daily_min_wind";
    private static final String DAILY_TIME_MIN_WIND = "daily_time_min_wind";
    private static final String DAILY_MAX_HUMID = "daily_max_humid";
    private static final String DAILY_TIME_MAX_HUMID = "daily_time_max_humid";
    private static final String DAILY_MIN_HUMID = "daily_min_humid";
    private static final String DAILY_TIME_MIN_HUMID = "daily_time_min_humid";
    private static final String DAILY_PRECIP = "daily_precip";
    private static final String DAILY_SUNRISE = "daily_sunrise";
    private static final String DAILY_SUNSET = "daily_sunset";
    private static final String DAILY_MOON_PHASE = "daily_moon_phase";
    private static final String DAILY_CONDITION_ICON = "daily_condition_icon";

    //Create daily table statement
    private static final String CREATE_DAILY_TABLE = "CREATE TABLE " + TABLE_DAILY + "(" + ID_KEY + " INTEGER PRIMARY KEY," + DAILY_DATE + " TEXT,"
            + DAILY_SUNRISE + " TEXT," + DAILY_SUNSET + " TEXT," + DAILY_MOON_PHASE + " TEXT," + DAILY_CONDITION_ICON + " INTEGER,"
            + DAILY_MAX_TEMP + " INTEGER," + DAILY_TIME_MAX_TEMP + " TEXT," + DAILY_MIN_TEMP + " INTEGER," + DAILY_TIME_MIN_TEMP + " TEXT,"
            + DAILY_MAX_WIND  + " INTEGER," +  DAILY_TIME_MAX_WIND + " TEXT," + DAILY_MIN_WIND + " INTEGER," + DAILY_TIME_MIN_WIND + " TEXT,"
            + DAILY_MAX_HUMID + " INTEGER," + DAILY_TIME_MAX_HUMID + " TEXT," + DAILY_MIN_HUMID + " INTEGER," + DAILY_TIME_MIN_HUMID + " TEXT," + DAILY_PRECIP + " INTEGER" +  ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_CURRENT_TABLE);
        db.execSQL(CREATE_HOURLY_TABLE);
        db.execSQL(CREATE_DAILY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOURLY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY);

        onCreate(db);
    }

    public boolean createCurrent(CurrentWeather currentWeather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CURRENT_TIME, currentWeather.getTimeUpdated());
        values.put(CURRENT_TEMP, currentWeather.getCurrentTemp());
        values.put(CURRENT_WIND_SPEED, currentWeather.getWindSpeed());
        values.put(CURRENT_FEELS_LIKE, currentWeather.getFeelsLikeTemp());
        db.insert(TABLE_CURRENT, null, values);
        return true;
    }

    public boolean updateCurrent(CurrentWeather currentWeather) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CURRENT_TIME, currentWeather.getTimeUpdated());
        values.put(CURRENT_TEMP, currentWeather.getCurrentTemp());
        values.put(CURRENT_WIND_SPEED, currentWeather.getWindSpeed());
        values.put(CURRENT_FEELS_LIKE, currentWeather.getFeelsLikeTemp());
        db.update(TABLE_CURRENT, values, "id = ?", new String[]{Integer.toString(1)});
        return true;
    }

    public CurrentWeather getCurrent() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_CURRENT + " WHERE "
                + ID_KEY + " = " + 1;

        Cursor cursor = db.rawQuery(selectQuery, null);



        if(cursor != null && cursor.moveToFirst()) {
            String time = cursor.getString(cursor.getColumnIndex(CURRENT_TIME));
            int tempValue = cursor.getInt(cursor.getColumnIndex(CURRENT_TEMP));
            int feelsLikeTemp = cursor.getInt(cursor.getColumnIndex(CURRENT_FEELS_LIKE));
            int windSpeed = cursor.getInt(cursor.getColumnIndex(CURRENT_WIND_SPEED));

            CurrentWeather currentWeatherObject = new CurrentWeather(time, tempValue, feelsLikeTemp, windSpeed);
            cursor.close();
            return currentWeatherObject;
        }
        else {
            assert cursor != null;
            cursor.close();
            return null;
        }

    }

    public boolean createHourly(HourlyWeather hour) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HOURLY_TIME, hour.getTimeString());
        values.put(HOURLY_TEMP, hour.getTemp());
        values.put(HOURLY_WIND, hour.getWindSpeed());
        values.put(HOURLY_PRECIP, hour.getPrecipChance());
        db.insert(TABLE_HOURLY, null, values);
        return true;
    }

    public boolean updateHourly(HourlyWeather hourlyWeather, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HOURLY_TIME, hourlyWeather.getTimeString());
        values.put(HOURLY_TEMP, hourlyWeather.getTemp());
        values.put(HOURLY_WIND, hourlyWeather.getWindSpeed());
        values.put(HOURLY_PRECIP, hourlyWeather.getPrecipChance());
        db.update(TABLE_HOURLY, values, "id = ?", new String[]{Integer.toString(id)});
        return true;
    }

    public HourlyWeather getHourly(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_HOURLY + " WHERE "
                + ID_KEY + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);



        if(cursor != null && cursor.moveToFirst()) {
            String time = cursor.getString(cursor.getColumnIndex(HOURLY_TIME));
            int temp = cursor.getInt(cursor.getColumnIndex(HOURLY_TEMP));
            int wind = cursor.getInt(cursor.getColumnIndex(HOURLY_WIND));
            int precip = cursor.getInt(cursor.getColumnIndex(HOURLY_PRECIP));

            HourlyWeather hourlyWeather = new HourlyWeather(time, temp, precip, wind);
            return hourlyWeather;
        }
        else
            return  null;
    }

    public ArrayList<HourlyWeather> getAllHourly() {
        ArrayList<HourlyWeather> hourlyWeatherArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_HOURLY, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            String time = cursor.getString(cursor.getColumnIndex(HOURLY_TIME));
            int temp = cursor.getInt(cursor.getColumnIndex(HOURLY_TEMP));
            int wind = cursor.getInt(cursor.getColumnIndex(HOURLY_WIND));
            int precip = cursor.getInt(cursor.getColumnIndex(HOURLY_PRECIP));
            HourlyWeather hourlyWeather = new HourlyWeather(time, temp, precip, wind);
            hourlyWeatherArrayList.add(hourlyWeather);
            cursor.moveToNext();
        }
        return hourlyWeatherArrayList;
    }

    public boolean createDaily(DailyWeather dailyWeather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DAILY_DATE, dailyWeather.getDate());
        contentValues.put(DAILY_SUNRISE, dailyWeather.getSunriseTime());
        contentValues.put(DAILY_SUNSET, dailyWeather.getSunsetTime());
        contentValues.put(DAILY_MOON_PHASE, dailyWeather.getMoonPhase());
        contentValues.put(DAILY_MAX_TEMP, dailyWeather.getMaxTemp());
        contentValues.put(DAILY_TIME_MAX_TEMP, dailyWeather.getTimeOfMaxTemp());
        contentValues.put(DAILY_MIN_TEMP, dailyWeather.getMinTemp());
        contentValues.put(DAILY_TIME_MIN_TEMP, dailyWeather.getTimeOfMinTemp());
        contentValues.put(DAILY_MAX_WIND, dailyWeather.getMaxWind());
        contentValues.put(DAILY_TIME_MAX_WIND, dailyWeather.getTimeOfMaxWind());
        contentValues.put(DAILY_MIN_WIND, dailyWeather.getMinWind());
        contentValues.put(DAILY_TIME_MIN_WIND, dailyWeather.getTimeOfMinWind());
        contentValues.put(DAILY_MAX_HUMID, dailyWeather.getMaxHumid());
        contentValues.put(DAILY_TIME_MAX_HUMID, dailyWeather.getTimeOfMaxHumid());
        contentValues.put(DAILY_MIN_HUMID, dailyWeather.getMinHumid());
        contentValues.put(DAILY_TIME_MIN_HUMID, dailyWeather.getTimeOfMinHumid());
        contentValues.put(DAILY_PRECIP, dailyWeather.getPrecipChance());
        contentValues.put(DAILY_CONDITION_ICON, dailyWeather.getConditionIcon());
        db.insert(TABLE_DAILY, null, contentValues);
        return true;
    }

    public boolean updateDaily(DailyWeather dailyWeather, int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DAILY_DATE, dailyWeather.getDate());
        contentValues.put(DAILY_SUNRISE, dailyWeather.getSunriseTime());
        contentValues.put(DAILY_SUNSET, dailyWeather.getSunsetTime());
        contentValues.put(DAILY_MOON_PHASE, dailyWeather.getMoonPhase());
        contentValues.put(DAILY_MAX_TEMP, dailyWeather.getMaxTemp());
        contentValues.put(DAILY_TIME_MAX_TEMP, dailyWeather.getTimeOfMaxTemp());
        contentValues.put(DAILY_MIN_TEMP, dailyWeather.getMinTemp());
        contentValues.put(DAILY_TIME_MIN_TEMP, dailyWeather.getTimeOfMinTemp());
        contentValues.put(DAILY_MAX_WIND, dailyWeather.getMaxWind());
        contentValues.put(DAILY_TIME_MAX_WIND, dailyWeather.getTimeOfMaxWind());
        contentValues.put(DAILY_MIN_WIND, dailyWeather.getMinWind());
        contentValues.put(DAILY_TIME_MIN_WIND, dailyWeather.getTimeOfMinWind());
        contentValues.put(DAILY_MAX_HUMID, dailyWeather.getMaxHumid());
        contentValues.put(DAILY_TIME_MAX_HUMID, dailyWeather.getTimeOfMaxHumid());
        contentValues.put(DAILY_MIN_HUMID, dailyWeather.getMinHumid());
        contentValues.put(DAILY_TIME_MIN_HUMID, dailyWeather.getTimeOfMinHumid());
        contentValues.put(DAILY_PRECIP, dailyWeather.getPrecipChance());
        contentValues.put(DAILY_CONDITION_ICON, dailyWeather.getConditionIcon());
        db.update(TABLE_DAILY, contentValues, "id = ?", new String[]{Integer.toString(id)});
        return true;
    }

    public DailyWeather getDaily(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DAILY + " WHERE "
                + ID_KEY + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null && cursor.moveToFirst()) {
            String date = cursor.getString(cursor.getColumnIndex(DAILY_DATE));
            String sunrise = cursor.getString(cursor.getColumnIndex(DAILY_SUNRISE));
            String sunset = cursor.getString(cursor.getColumnIndex(DAILY_SUNSET));
            String moon = cursor.getString(cursor.getColumnIndex(DAILY_MOON_PHASE));
            int maxTemp = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_TEMP));
            String timeMaxTemp = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_TEMP));
            int minTemp = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_TEMP));
            String timeMinTemp = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_TEMP));
            int maxWind = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_WIND));
            String timeMaxWind = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_WIND));
            int minWind = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_WIND));
            String timeMinWind = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_WIND));
            int maxHumid = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_HUMID));
            String timeMaxHumid = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_HUMID));
            int minHumid = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_HUMID));
            String timeMinHumid = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_HUMID));
            int dailyPrecip = cursor.getInt(cursor.getColumnIndex(DAILY_PRECIP));
            int conditionIcon = cursor.getInt(cursor.getColumnIndex(DAILY_CONDITION_ICON));

            DailyWeather dailyWeather = new DailyWeather(minTemp, maxTemp, minHumid, maxHumid, minWind, maxWind, dailyPrecip, timeMinTemp, timeMaxTemp, timeMinHumid, timeMaxHumid, timeMinWind, timeMaxWind, sunrise, sunset, moon, date, conditionIcon);
            return  dailyWeather;
        }
        else
            return null;
    }

    public ArrayList<DailyWeather> getAllDaily() {
        ArrayList<DailyWeather> dailyWeatherArrayList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_DAILY, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String date = cursor.getString(cursor.getColumnIndex(DAILY_DATE));
            String sunrise = cursor.getString(cursor.getColumnIndex(DAILY_SUNRISE));
            String sunset = cursor.getString(cursor.getColumnIndex(DAILY_SUNSET));
            String moon = cursor.getString(cursor.getColumnIndex(DAILY_MOON_PHASE));
            int maxTemp = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_TEMP));
            String timeMaxTemp = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_TEMP));
            int minTemp = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_TEMP));
            String timeMinTemp = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_TEMP));
            int maxWind = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_WIND));
            String timeMaxWind = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_WIND));
            int minWind = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_WIND));
            String timeMinWind = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_WIND));
            int maxHumid = cursor.getInt(cursor.getColumnIndex(DAILY_MAX_HUMID));
            String timeMaxHumid = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MAX_HUMID));
            int minHumid = cursor.getInt(cursor.getColumnIndex(DAILY_MIN_HUMID));
            String timeMinHumid = cursor.getString(cursor.getColumnIndex(DAILY_TIME_MIN_HUMID));
            int dailyPrecip = cursor.getInt(cursor.getColumnIndex(DAILY_PRECIP));
            int conditionIcon = cursor.getInt(cursor.getColumnIndex(DAILY_CONDITION_ICON));

            DailyWeather dailyWeather = new DailyWeather(minTemp, maxTemp, minHumid, maxHumid, minWind, maxWind, dailyPrecip, timeMinTemp, timeMaxTemp, timeMinHumid, timeMaxHumid, timeMinWind, timeMaxWind, sunrise, sunset, moon, date, conditionIcon);

            dailyWeatherArrayList.add(dailyWeather);
            cursor.moveToNext();
        }
        cursor.close();
        return dailyWeatherArrayList;
    }
}

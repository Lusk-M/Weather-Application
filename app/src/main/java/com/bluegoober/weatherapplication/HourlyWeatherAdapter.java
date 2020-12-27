package com.bluegoober.weatherapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView timeTextView, tempTextView, precipTextView, windTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            timeTextView = itemView.findViewById(R.id.hourly_time_view);
            tempTextView = itemView.findViewById(R.id.hourly_temp_view);
            precipTextView = itemView.findViewById(R.id.hourly_precip_view);
            windTextView = itemView.findViewById(R.id.hourly_wind_view);
        }
    }

    private List<HourlyWeather> weatherHours;

    public HourlyWeatherAdapter(List<HourlyWeather> hourly) {
        weatherHours = hourly;
    }

    @Override
    public HourlyWeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View hourlyView = inflater.inflate(R.layout.hourly_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(hourlyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourlyWeatherAdapter.ViewHolder holder, int position) {
        HourlyWeather hourly = weatherHours.get(position);

        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        LocalDateTime timeFormat = LocalDateTime.parse(hourly.getTimeString(), inputFormatter);
        String actualLocalTime = timeFormat.atZone(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("h:mm a").withZone(tz.toZoneId()));

        TextView timeView = holder.timeTextView;
        timeView.setText(actualLocalTime);

        TextView tempView = holder.tempTextView;
        String tempString = hourly.getTemp() + hourly.getTempUnit();
        tempView.setText(tempString);

        TextView precipView = holder.precipTextView;
        String precipString = hourly.getPrecipChance() + hourly.getPrecipUnit();
        precipView.setText(precipString);

        TextView windView = holder.windTextView;
        String windString = hourly.getWindSpeed() + " " + hourly.getWindUnit();
        windView.setText(windString);
    }

    @Override
    public int getItemCount() {
        return weatherHours.size();
    }


}

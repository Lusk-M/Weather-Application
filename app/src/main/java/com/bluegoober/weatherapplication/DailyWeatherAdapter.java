package com.bluegoober.weatherapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView dayDateView, minTempView, maxTempView, dayPrecipView, dayWindView, dayHumidView;

        public ViewHolder(View itemView) {
            super(itemView);

            dayDateView = itemView.findViewById(R.id.day_date_view);
            minTempView = itemView.findViewById(R.id.day_min_temp_view);
            maxTempView = itemView.findViewById(R.id.day_max_temp_view);
            dayPrecipView = itemView.findViewById(R.id.day_precip_view);
            dayWindView = itemView.findViewById(R.id.day_wind_view);
            dayHumidView = itemView.findViewById(R.id.day_humid_view);

        }
    }

    private List<DailyWeather> weatherDays;

    public DailyWeatherAdapter(List<DailyWeather> daily) {
        weatherDays = daily;
    }

    @Override
    public DailyWeatherAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View dailyView = inflater.inflate(R.layout.day_card_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(dailyView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DailyWeatherAdapter.ViewHolder holder, final int position) {
        DailyWeather days = weatherDays.get(position);
        final View cardView = holder.itemView;


        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E\nMM/dd");
        LocalDate dateFormat = LocalDate.parse(days.getDate(), inputFormatter);
        String formattedDate = dateFormat.format(formatter);


        TextView dateView = holder.dayDateView;
        dateView.setText(formattedDate);

        TextView tempViewMin = holder.minTempView;
        tempViewMin.setText(days.getMinTemp() + days.getMinTempUnit());
        TextView tempViewMax = holder.maxTempView;
        tempViewMax.setText(days.getMaxTemp() + days.getMaxTempUnit());

        TextView humidView = holder.dayHumidView;
        humidView.setText(days.getMaxHumid() + days.getMaxHumidUnit());

        TextView precipView = holder.dayPrecipView;
        precipView.setText(days.getPrecipChance() + days.getPrecipUnit());

        TextView windView = holder.dayWindView;
        windView.setText(days.getMaxWind() + " " + days.getMaxWindUnit());

        //Set an onClickListener to the CardView which runs an intent to the DetailedWeatherActivity
        //Include the weather day ID as an extra in the intent
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(cardView.getContext(), DetailedWeatherActivity.class);
                intent.putExtra(DetailedWeatherActivity.DETAILED_COUNTDOWN_ID,position+1);
                cardView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherDays.size();
    }


}

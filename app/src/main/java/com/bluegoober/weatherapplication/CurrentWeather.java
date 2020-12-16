package com.bluegoober.weatherapplication;

public class CurrentWeather {
    private String timeUpdated;
    int currentTemp;
    int feelsLikeTemp;
    int windSpeed;

    public CurrentWeather(String timeUpdated, int currentTemp, int feelsLikeTemp, int windSpeed) {
        this.timeUpdated = timeUpdated;
        this.currentTemp = currentTemp;
        this.feelsLikeTemp = feelsLikeTemp;
        this.windSpeed = windSpeed;
    }

    public String getTimeUpdated() {
        return timeUpdated;
    }

    public void setTimeUpdated(String timeUpdated) {
        this.timeUpdated = timeUpdated;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getFeelsLikeTemp() {
        return feelsLikeTemp;
    }

    public void setFeelsLikeTemp(int feelsLikeTemp) {
        this.feelsLikeTemp = feelsLikeTemp;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }
}

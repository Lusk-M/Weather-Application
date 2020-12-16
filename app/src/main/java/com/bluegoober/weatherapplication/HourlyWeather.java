package com.bluegoober.weatherapplication;

public class HourlyWeather {
    String timeString, tempUnit, precipUnit, windUnit;
    int temp, precipChance, windSpeed;

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

    public String getTempUnit() {
        return tempUnit;
    }

    public void setTempUnit(String tempUnit) {
        this.tempUnit = tempUnit;
    }

    public String getPrecipUnit() {
        return precipUnit;
    }

    public void setPrecipUnit(String precipUnit) {
        this.precipUnit = precipUnit;
    }

    public String getWindUnit() {
        return windUnit;
    }

    public void setWindUnit(String windUnit) {
        this.windUnit = windUnit;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public HourlyWeather(String timeString, int temp, int precipChance, int windSpeed) {
        this.timeString = timeString;
        this.temp = temp;
        this.precipChance = precipChance;
        this.windSpeed = windSpeed;
        this.tempUnit ="F";
        this.precipUnit = "%";
        this.windUnit = "mph";
    }

    public int getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(int precipChance) {
        this.precipChance = precipChance;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public HourlyWeather(String timeString, String tempUnit, String precipUnit, String windUnit, int temp, int precipChance, int windSpeed) {
        this.timeString = timeString;
        this.tempUnit = tempUnit;
        this.precipUnit = precipUnit;
        this.windUnit = windUnit;
        this.temp = temp;
        this.precipChance = precipChance;
        this.windSpeed = windSpeed;
    }
}

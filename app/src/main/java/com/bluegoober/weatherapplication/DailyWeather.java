package com.bluegoober.weatherapplication;

public class DailyWeather {

    int minTemp, maxTemp, minHumid, maxHumid, minWind, maxWind;
    int precipChance, conditionIcon;
    String timeOfMinTemp, minTempUnit, timeOfMaxTemp, maxTempUnit;
    String precipUnit;
    String timeOfMinHumid, minHumidUnit, timeOfMaxHumid, maxHumidUnit;
    String timeOfMinWind, minWindUnit, timeOfMaxWind, maxWindUnit;
    String sunriseTime;
    String sunsetTime;
    String moonPhase;
    String date;

    public DailyWeather(int minTemp, int maxTemp, int minHumid, int maxHumid, int minWind, int maxWind, int precipChance, String timeOfMinTemp, String timeOfMaxTemp, String timeOfMinHumid, String timeOfMaxHumid, String timeOfMinWind, String timeOfMaxWind, String sunriseTime, String sunsetTime, String moonPhase, String date, int conditionIcon) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHumid = minHumid;
        this.maxHumid = maxHumid;
        this.minWind = minWind;
        this.maxWind = maxWind;
        this.precipChance = precipChance;
        this.timeOfMinTemp = timeOfMinTemp;
        this.timeOfMaxTemp = timeOfMaxTemp;
        this.timeOfMinHumid = timeOfMinHumid;
        this.timeOfMaxHumid = timeOfMaxHumid;
        this.timeOfMinWind = timeOfMinWind;
        this.timeOfMaxWind = timeOfMaxWind;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.moonPhase = moonPhase;
        this.date = date;
        this.conditionIcon = conditionIcon;
        this.maxTempUnit = "F";
        this.minTempUnit = "F";
        this.minHumidUnit = "%";
        this.maxHumidUnit = "%";
        this.maxWindUnit = "mph";
        this.minWindUnit = "mph";
        this.precipUnit = "%";
    }

    public DailyWeather(int minTemp, int maxTemp, int minHumid, int maxHumid, int minWind, int maxWind, int precipChance, String timeOfMinTemp, String minTempUnit, String timeOfMaxTemp, String maxTempUnit, String precipUnit, String timeOfMinHumid, String minHumidUnit, String timeOfMaxHumid, String maxHumidUnit, String timeOfMinWind, String minWindUnit, String timeOfMaxWind, String maxWindUnit, String date) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minHumid = minHumid;
        this.maxHumid = maxHumid;
        this.minWind = minWind;
        this.maxWind = maxWind;
        this.precipChance = precipChance;
        this.timeOfMinTemp = timeOfMinTemp;
        this.minTempUnit = minTempUnit;
        this.timeOfMaxTemp = timeOfMaxTemp;
        this.maxTempUnit = maxTempUnit;
        this.precipUnit = precipUnit;
        this.timeOfMinHumid = timeOfMinHumid;
        this.minHumidUnit = minHumidUnit;
        this.timeOfMaxHumid = timeOfMaxHumid;
        this.maxHumidUnit = maxHumidUnit;
        this.timeOfMinWind = timeOfMinWind;
        this.minWindUnit = minWindUnit;
        this.timeOfMaxWind = timeOfMaxWind;
        this.maxWindUnit = maxWindUnit;
        this.date = date;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int macTemp) {
        this.maxTemp = macTemp;
    }

    public int getMinHumid() {
        return minHumid;
    }

    public void setMinHumid(int minHumid) {
        this.minHumid = minHumid;
    }

    public int getMaxHumid() {
        return maxHumid;
    }

    public void setMaxHumid(int maxHumid) {
        this.maxHumid = maxHumid;
    }

    public int getMinWind() {
        return minWind;
    }

    public void setMinWind(int minWind) {
        this.minWind = minWind;
    }

    public int getMaxWind() {
        return maxWind;
    }

    public void setMaxWind(int maxWind) {
        this.maxWind = maxWind;
    }

    public int getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(int precipChance) {
        this.precipChance = precipChance;
    }

    public int getConditionIcon() {
        return conditionIcon;
    }

    public void setConditionIcon(int conditionIcon) {
        this.precipChance = conditionIcon;
    }

    public String getTimeOfMinTemp() {
        return timeOfMinTemp;
    }

    public void setTimeOfMinTemp(String timeOfMinTemp) {
        this.timeOfMinTemp = timeOfMinTemp;
    }

    public String getMinTempUnit() {
        return minTempUnit;
    }

    public void setMinTempUnit(String minTempUnit) {
        this.minTempUnit = minTempUnit;
    }

    public String getTimeOfMaxTemp() {
        return timeOfMaxTemp;
    }

    public void setTimeOfMaxTemp(String timeOfMaxTemp) {
        this.timeOfMaxTemp = timeOfMaxTemp;
    }

    public String getMaxTempUnit() {
        return maxTempUnit;
    }

    public void setMaxTempUnit(String maxTempUnit) {
        this.maxTempUnit = maxTempUnit;
    }

    public String getPrecipUnit() {
        return precipUnit;
    }

    public void setPrecipUnit(String precipUnit) {
        this.precipUnit = precipUnit;
    }

    public String getTimeOfMinHumid() {
        return timeOfMinHumid;
    }

    public void setTimeOfMinHumid(String timeOfMinHumid) {
        this.timeOfMinHumid = timeOfMinHumid;
    }

    public String getMinHumidUnit() {
        return minHumidUnit;
    }

    public void setMinHumidUnit(String minHumidUnit) {
        this.minHumidUnit = minHumidUnit;
    }

    public String getTimeOfMaxHumid() {
        return timeOfMaxHumid;
    }

    public void setTimeOfMaxHumid(String timeOfMaxHumid) {
        this.timeOfMaxHumid = timeOfMaxHumid;
    }

    public String getMaxHumidUnit() {
        return maxHumidUnit;
    }

    public void setMaxHumidUnit(String maxHumidUnit) {
        this.maxHumidUnit = maxHumidUnit;
    }

    public String getTimeOfMinWind() {
        return timeOfMinWind;
    }

    public void setTimeOfMinWind(String timeOfMinWind) {
        this.timeOfMinWind = timeOfMinWind;
    }

    public String getMinWindUnit() {
        return minWindUnit;
    }

    public void setMinWindUnit(String minWindUnit) {
        this.minWindUnit = minWindUnit;
    }

    public String getTimeOfMaxWind() {
        return timeOfMaxWind;
    }

    public void setTimeOfMaxWind(String timeOfMaxWind) {
        this.timeOfMaxWind = timeOfMaxWind;
    }

    public String getMaxWindUnit() {
        return maxWindUnit;
    }

    public void setMaxWindUnit(String maxWindUnit) {
        this.maxWindUnit = maxWindUnit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(String sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public String getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(String sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(String moonPhase) {
        this.moonPhase = moonPhase;
    }
}

package org.csw.narsi2;

import com.google.firebase.firestore.FieldValue;

public class WeatherInfo {

    private String temp, nowWeather, airPollutionNow, tmax, tmin, humidity, wspd, wctIndex;
    private FieldValue fieldValue;

    public WeatherInfo() {
    }


    public WeatherInfo(String temp, String nowWeather, String airPollutionNow, String tmax, String tmin, String humidity, String wspd, String wctIndex, FieldValue fieldValue) {
        this.temp = temp;
        this.nowWeather = nowWeather;
        this.airPollutionNow = airPollutionNow;
        this.tmax = tmax;
        this.tmin = tmin;
        this.humidity = humidity;
        this.wspd = wspd;
        this.wctIndex = wctIndex;
        this.fieldValue = fieldValue;
    }

    public String getTemp() {
        return temp;
    }

    public String getNowWeather() {
        return nowWeather;
    }

    public String getHumidity() {
        return humidity;
    }

    public FieldValue getFieldValue() {
        return fieldValue;
    }

    public String getAirPollutionNow() {
        return airPollutionNow;
    }

    public String getTmax() {
        return tmax;
    }

    public String getTmin() {
        return tmin;
    }

    public String getWctIndex() {
        return wctIndex;
    }

    public String getWspd() {
        return wspd;
    }
}

package org.csw.narsi2;

public class Weather {
    private static final Weather ourInstance = new Weather();
    private double temp;
    private String airPollution;
    private double humidity;
    private String whereGu;
    private double tmin, tmax;
    private double wspd;
    private double wctindex;
    private String nowWeather;

    public static Weather getInstance() {
        return ourInstance;
    }

    private Weather() {
    }

    public void setWeather(String whereGu, String nowWeather, String airPollution, double temp, double tmax, double tmin, double wspd, double wctindex, double humidity) {
        this.setNowWeather(nowWeather);
        this.setWctindex(wctindex);
        this.setWspd(wspd);
        this.setTmax(tmax);
        this.setTmin(tmin);
        this.setAirPollution(airPollution);
        this.setHumidity(humidity);
        this.setWhereGu(whereGu);
        this.setTemp(temp);
    }

    public String getNowWeather() {
        return nowWeather;
    }

    public void setNowWeather(String nowWeather) {
        this.nowWeather = nowWeather;
    }


    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getAirPollution() {
        return airPollution;
    }

    public void setAirPollution(String airPollution) {
        this.airPollution = airPollution;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public String getWhereGu() {
        return whereGu;
    }

    public void setWhereGu(String whereGu) {
        this.whereGu = whereGu;
    }

    public double getTmin() {
        return tmin;
    }

    public void setTmin(double tmin) {
        this.tmin = tmin;
    }

    public double getTmax() {
        return tmax;
    }

    public void setTmax(double tmax) {
        this.tmax = tmax;
    }

    public double getWspd() {
        return wspd;
    }

    public void setWspd(double wspd) {
        this.wspd = wspd;
    }

    public double getWctindex() {
        return wctindex;
    }

    public void setWctindex(double wctindex) {
        this.wctindex = wctindex;
    }
}

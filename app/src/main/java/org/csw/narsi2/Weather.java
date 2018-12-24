package org.csw.narsi2;

public class Weather {

    private String temperature, highestTemp, lowestTemp, pm10value, pm25value, Precipitation, wspd, avgTemperatrue, avgWindspeed, waterType;
    public void Weather(){}

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHighestTemp() {
        return highestTemp;
    }

    public void setHighestTemp(String highestTemp) {
        this.highestTemp = highestTemp;
    }

    public String getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(String lowestTemp) {
        this.lowestTemp = lowestTemp;
    }

    public String getPm10value() {
        return pm10value;
    }

    public void setPm10value(String pm10value) {
        this.pm10value = pm10value;
    }

    public String getPm25value() {
        return pm25value;
    }

    public void setPm25value(String pm25value) {
        this.pm25value = pm25value;
    }


    public String getPrecipitation() {
        return Precipitation;
    }

    public void setPrecipitation(String precipitation) {
        Precipitation = precipitation;
    }

    public String getWspd() {
        return wspd;
    }

    public void setWspd(String wspd) {
        this.wspd = wspd;
    }

    public String getAvgTemperatrue() {
        return avgTemperatrue;
    }

    public void setAvgTemperatrue(String avgTemperatrue) {
        this.avgTemperatrue = avgTemperatrue;
    }

    public String getAvgWindspeed() {
        return avgWindspeed;
    }

    public void setAvgWindspeed(String avgWindspeed) {
        this.avgWindspeed = avgWindspeed;
    }

    public String getWaterType() {
        return waterType;
    }

    public void setWaterType(String waterType) {
        this.waterType = waterType;
    }
}

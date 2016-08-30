package com.google.youhai.myinterviewproject.Utilities;

/**
 * Created by youhai on 8/13/16.
 */
public class ForecastModel {

    private String main;
    private String date;
    private double minTemp;
    private double maxTemp;
    private double AveTemp;
    private String icon;
    private double humidity;
    private double pressure;

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public String getMain() {
        return main;
    }

    public String getDate() {
        return date;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getAveTemp() {
        return AveTemp;
    }

    public void setAveTemp(double aveTemp) {
        AveTemp = aveTemp;
    }
}

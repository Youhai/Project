package com.google.youhai.myinterviewproject.Utilities;

/**
 * Created by youhai on 8/13/16.
 */
public class UrlController {

    public final static String BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    public final static String URL_LAT = "lat=";
    public final static String URL_LNG = "&lon=";
    public final static String APP_ID = "&appid=f25557d990f8043177a9642d2635e0c3";
    public final static String ICON_URL = "http://openweathermap.org/img/w/";
    public final static String ICON_TAIL =".png";

    public static String callByLatLon(double Lat, double Lon) {
        return BASE_URL  + URL_LAT + Lat + URL_LNG+ Lon + APP_ID;
    }

    public static String callByIcon(String icon){
        return ICON_URL+icon+ICON_TAIL;
    }

    public static String callByCity(String city) {
        city = city.replaceAll(" ","");
        return BASE_URL  + "q=" + city + APP_ID;
    }

    public static String callByCity(String city, String country) {

        return BASE_URL  + "q=" + city + "," + country + APP_ID;
    }

    public static String callByZipCode(int code, String country) {
        return BASE_URL +  "zip=" + code + "," + country + APP_ID;
    }


    }
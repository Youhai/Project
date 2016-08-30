package com.google.youhai.myinterviewproject.Utilities;

import com.google.youhai.myinterviewproject.Application.DataCenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;


public class WeatherJSONParser {

    private ArrayList<ForecastModel> forecastModelsList;

    public ArrayList<ForecastModel> parseResponseFromLatLong(JSONObject response){
        forecastModelsList = new ArrayList<>();
        try {
            JSONArray list = response.getJSONArray("list");
            if (list != null){

                for (int i = 0; i < list.length(); i ++){
                    JSONObject aDay = list.getJSONObject(i);
                    ForecastModel forecastModel = new ForecastModel();

                    if (aDay!= null){
                        //parse main weather information
                        JSONArray weather = aDay.getJSONArray("weather");
                        JSONObject jsonObject = weather.getJSONObject(0);
                        String main = jsonObject.getString("main");
                        String icon = jsonObject.getString("icon");


                        //parse date information
                        int dtime = aDay.getInt("dt");
                        Date date = new Date(dtime*1000L); // *1000 is to convert seconds to milliseconds
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
                        sdf.setTimeZone(TimeZone.getTimeZone("UTC-8")); // give a timezone reference for formating (see comment at the botto
                        String formattedDate = sdf.format(date);


                        // parse the temperature information and stored as Celsius format
                        JSONObject temp = aDay.getJSONObject("temp");
                        String eveTemp = temp.getString("eve");
                        String eve = convertTemp(eveTemp);
                        String day = temp.getString("day");
                        String min = temp.getString("min");
                        String max = temp.getString("max");


                        //parse the humidity in this day
                        double humidity = aDay.getDouble("humidity");

                        //parse the pressure in thus day
                        double pressure = aDay.getDouble("pressure");

                        forecastModel.setDate(formattedDate);
                        forecastModel.setMain(main);
                        forecastModel.setIcon(icon);
                        forecastModel.setAveTemp(Double.parseDouble(convertTemp(day)));
                        forecastModel.setMinTemp(Double.parseDouble(convertTemp(min)));
                        forecastModel.setMaxTemp(Double.parseDouble(convertTemp(max)));
                        forecastModel.setHumidity(humidity);
                        forecastModel.setPressure(pressure);
                        forecastModelsList.add(forecastModel);
                    }
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }
        return forecastModelsList;
    }

    private String convertTemp(String eveTemp) {
        //kevin temperature format into Celsius
        double c_temp = Double.parseDouble(eveTemp);
        c_temp -= DataCenter.TEMP_INDEX;
        DecimalFormat dt = new DecimalFormat("0.00");
        return dt.format(c_temp);
    }
}

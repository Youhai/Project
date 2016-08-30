package com.google.youhai.myinterviewproject.Application;


import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.youhai.myinterviewproject.Utilities.ForecastModel;

import java.util.ArrayList;

public class DataCenter {

    private static DataCenter instance;
    private LatLng mCurrentLocation;
    private LatLng mChosenLocation;
    private CameraPosition cameraPosition;
    public static double TEMP_INDEX = 273.15;

    ArrayList<ForecastModel> forecastModelArrayList;

    /***************************
     * Constructors
     **************************/

    private DataCenter() {
    }

    /***************************
     * Public Methods
     **************************/

    /**
     * The singleton data center for easy data access throughout the application
     * e.g. Last known location, App State, & others, etc.
     *
     * @return the instance of DataStore
     */
    public static synchronized DataCenter getInstance() {
        if (instance == null) {
            instance = new DataCenter();
        }
        return instance;
    }
    public String getLocationApiUrl(String placeId) {
        return "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=AIzaSyCopt39Zj6FQhGx0H5FfNCvmVZPJOZyuA0";
    }
    public LatLng getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(LatLng mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    public LatLng getChosenLocation() {
        return mChosenLocation;
    }

    public void setChosenLocation(LatLng mChosenLocation) {
        this.mChosenLocation = mChosenLocation;
    }

    public ArrayList<ForecastModel> getForecastModelArrayList() {
        return forecastModelArrayList;
    }

    public void setForecastModelArrayList(ArrayList<ForecastModel> forecastModelArrayList) {
        this.forecastModelArrayList = forecastModelArrayList;
    }

    public CameraPosition getCameraPosition() {
        return cameraPosition;
    }

    public void setCameraPosition(CameraPosition cameraPosition) {
        this.cameraPosition = cameraPosition;
    }
}

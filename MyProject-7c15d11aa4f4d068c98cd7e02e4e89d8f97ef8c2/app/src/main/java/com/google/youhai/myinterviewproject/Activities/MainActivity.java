package com.google.youhai.myinterviewproject.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.youhai.myinterviewproject.Application.PermissionControl;
import com.google.youhai.myinterviewproject.Utilities.PlaceModel;
import com.google.youhai.myinterviewproject.Adapters.PlacesAutoCompleteAdapter;
import com.google.youhai.myinterviewproject.R;
import com.google.youhai.myinterviewproject.Utilities.UrlController;
import com.google.youhai.myinterviewproject.Application.DataCenter;
import com.google.youhai.myinterviewproject.Utilities.ForecastModel;
import com.google.youhai.myinterviewproject.Application.VolleyManager;
import com.google.youhai.myinterviewproject.Utilities.WeatherJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    //defined constants
    private static final int PERMISSION_REQUEST_STORAGE = 1;
    private static final int PERMISSION_REQUEST_LOCATION = 2;
    private final String TAG = "MainActivity";
    private static final String LAT_LON_KEY = "LAT_LON_KEY";


    //class attributes
    private LatLng chosenLocation;
    private LatLng selectedLocation;
    private AutoCompleteTextView searchPlace;
    private PlaceModel selectedPlace;
    private GoogleMap googleMap;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(LAT_LON_KEY, chosenLocation);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        chosenLocation = savedInstanceState.getParcelable(LAT_LON_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        View mapView = mapFragment.getView();


        TextView checkForecast = (TextView) findViewById(R.id.check_forecast);
        searchPlace = (AutoCompleteTextView) findViewById(R.id.search_places);

        checkForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ForecastDetailsActivity.class);
                startActivity(intent);
            }
        });

        getCurrentLocation();
        chosenLocation = DataCenter.getInstance().getChosenLocation();
        if (chosenLocation == null) {
            chosenLocation = DataCenter.getInstance().getCurrentLocation();
            DataCenter.getInstance().setChosenLocation(chosenLocation);
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpPlace();
    }

    private void setUpPlace() {
        //Use Web service to request auto complete
        searchPlace.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.search_list_item));
        //Get the location when user selects an address from the search suggestion
        searchPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPlace = (PlaceModel) parent.getItemAtPosition(position);
                searchPlace.setText(selectedPlace.getDescription());

                if (null != selectedPlace) {
                    //Hide keyboard on item clicked
                    view = getCurrentFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert view != null;
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    String url = DataCenter.getInstance().getLocationApiUrl(selectedPlace.getPlaceID());
                    getPlaceTask(url);
                }
            }
        });

    }

    private void getPlaceTask(String url) {
        Log.d(TAG, "volley request: " + url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: " + response.toString());
                try {
                    JSONObject result = response.getJSONObject("result");
                    JSONObject geometry = result.getJSONObject("geometry");

                    JSONObject location = geometry.getJSONObject("location");
                    selectedLocation = new LatLng(location.getDouble("lat"), location.getDouble("lng"));

                    if (selectedLocation.latitude != 0.0 && selectedLocation.longitude != 0.0) {
                        Log.d(TAG, "Valid selected location");
                        DataCenter.getInstance().setChosenLocation(selectedLocation);
                        getForecastTask(selectedLocation);

                    } else {
                        searchPlace.setText("");
                        selectedPlace = null;
                        selectedLocation = null;
                        Log.e(TAG, "Invalid selected location");
                        Toast.makeText(MainActivity.this, "Network Error, Please Try Again!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse error " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "JsonObjectRequest - GetLocationTask " + error.toString());
            }
        });
        VolleyManager.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);

    }

    private void getForecastTask(LatLng location) {
        String weatherUrl = UrlController.callByLatLon(location.latitude, location.longitude);
        JsonObjectRequest customJsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, weatherUrl,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        WeatherJSONParser parser = new WeatherJSONParser();
                        ArrayList<ForecastModel> forecastModelArrayList = parser.parseResponseFromLatLong(response);
                        DataCenter.getInstance().setForecastModelArrayList(forecastModelArrayList);

                        String weatherState = forecastModelArrayList.get(0).getMain();
                        if (googleMap != null) {
                            googleMap.setInfoWindowAdapter(new ShowWeatherInfoWindowAdapter(MainActivity.this, weatherState));

                        }


                        centerMapWithChosenLocation(DataCenter.getInstance().getChosenLocation(), googleMap);
                        MarkerOptions options = new MarkerOptions();
                        options.position(DataCenter.getInstance().getChosenLocation());
                        Marker selectedMarker = googleMap.addMarker(options);
                        selectedMarker.showInfoWindow();
                        selectedMarker.setDraggable(true);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Time Out Error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error in getting Direction: " + error.toString());
            }
        }
        );
        VolleyManager.getInstance(MainActivity.this).addToRequestQueue(customJsonObjectRequest);


    }


    private void getCurrentLocation() {
        PermissionControl.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_REQUEST_LOCATION);
        PermissionControl.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_LOCATION);
        if (PermissionControl.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            try {
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(bestProvider, 2000, 0, mLocationListener);
                Location currentLocation = locationManager.getLastKnownLocation(bestProvider);

                if (currentLocation != null) {
                    DataCenter.getInstance().setCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    Log.d(TAG, DataCenter.getInstance().getCurrentLocation().toString());

                } else {
                    Log.e(TAG, "Can't get current location");
                }
            } catch (SecurityException e) {
                Log.e(TAG, "Location Manager Error: " + e.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "no location permission granted", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.

                return;
            }
            case PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //get current location
                } else {

                }

            }


        }
    }


    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                Location currentLocation = locationManager.getLastKnownLocation(bestProvider);
                DataCenter.getInstance().setCurrentLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                DataCenter.getInstance().setChosenLocation(latLng);
                centerMapWithChosenLocation(latLng,googleMap);
                getForecastTask(latLng);
            }
        });
        try {
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        centerMapWithChosenLocation(DataCenter.getInstance().getChosenLocation(),googleMap);
        getForecastTask(DataCenter.getInstance().getChosenLocation());
    }




    private class ShowWeatherInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private  View view;
        private  TextView markerTitle;
        private String weatherState;
        private Context context;

        public ShowWeatherInfoWindowAdapter(Context context, String weatherState) {
           this.context = context;
            this.weatherState = weatherState;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            LayoutInflater LayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = LayoutInflater.inflate(R.layout.custom_marker_layout,null);
            markerTitle = (TextView) view.findViewById(R.id.tv_marker_title);
            markerTitle.setText(weatherState);
            Log.e(TAG,weatherState);
            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return view;
        }
    }

    private void centerMapWithChosenLocation(LatLng chosenLocation, GoogleMap googleMap) {
        CameraPosition cameraPosition;
        googleMap.clear();
        cameraPosition = new CameraPosition.Builder().target(chosenLocation).zoom(15).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


}



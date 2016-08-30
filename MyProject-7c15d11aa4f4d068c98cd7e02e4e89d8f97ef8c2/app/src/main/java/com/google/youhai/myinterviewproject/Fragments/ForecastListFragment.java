package com.google.youhai.myinterviewproject.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.youhai.myinterviewproject.Adapters.ForecastAdapter;
import com.google.youhai.myinterviewproject.Application.DataCenter;
import com.google.youhai.myinterviewproject.R;
import com.google.youhai.myinterviewproject.Utilities.ForecastModel;

import java.util.ArrayList;


public class ForecastListFragment extends Fragment {
    private ListView forecastList;
    private ForecastAdapter forecastAdapter;
    private ArrayList<ForecastModel> forecastModelArrayList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_list, container, false);

        forecastList = (ListView)view.findViewById(R.id.list_forecast);
        forecastModelArrayList = DataCenter.getInstance().getForecastModelArrayList();
        if (forecastModelArrayList != null){
            forecastAdapter = new ForecastAdapter(getActivity(), forecastModelArrayList);
            forecastList.setAdapter(forecastAdapter);
            forecastAdapter.notifyDataSetChanged();
        }
        return view;
    }
}

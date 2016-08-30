package com.google.youhai.myinterviewproject.Adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.youhai.myinterviewproject.R;
import com.google.youhai.myinterviewproject.Utilities.ForecastModel;
import com.google.youhai.myinterviewproject.Utilities.UrlController;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ForecastAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ForecastModel> forecastModels;

    public ForecastAdapter(Context context, ArrayList<ForecastModel> forecastModels){
        this.context = context;
        this.forecastModels = forecastModels;
    }

    @Override
    public int getCount() {
        return forecastModels.size();
    }

    @Override
    public Object getItem(int position) {
        return forecastModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForecastViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ForecastViewHolder();
            LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.forecast_item_layout, parent, false);
            viewHolder.ivForecast = (ImageView) convertView.findViewById(R.id.iv_forecast);
            viewHolder.tvMain = (TextView) convertView.findViewById(R.id.tv_main);
            viewHolder.tvAveTemp = (TextView) convertView.findViewById(R.id.tv_temp);
            viewHolder.tvDate = (TextView)convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ForecastViewHolder) convertView.getTag();
        }

        /// initialized the data to view

        ForecastModel forecastModel = forecastModels.get(position);
        String icon_url = UrlController.callByIcon(forecastModel.getIcon());
        //implement Picasso to loading icon
        Picasso.with(context) //
                .load(icon_url) //
                .placeholder(R.drawable.cloudy) //
                .error(R.drawable.storm) //
                .fit() //
                .tag(context) //
                .into(viewHolder.ivForecast);

        viewHolder.tvMain.setText(forecastModel.getMain());
        viewHolder.tvAveTemp.setText(forecastModel.getAveTemp()+"°C");
        viewHolder.tvDate.setText(forecastModel.getDate());
        return convertView;
    }

    static class ForecastViewHolder {
        ImageView ivForecast;
        TextView tvMain;
        TextView tvAveTemp;
        TextView tvDate;
    }
}

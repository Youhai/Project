package com.google.youhai.myinterviewproject.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.youhai.myinterviewproject.Adapters.WeatherViewPagerAdapter;
import com.google.youhai.myinterviewproject.Fragments.ChartFragment;
import com.google.youhai.myinterviewproject.Fragments.ForecastListFragment;
import com.google.youhai.myinterviewproject.R;


public class ForecastDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast_details);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        WeatherViewPagerAdapter weatherViewPagerAdapter = new WeatherViewPagerAdapter(getSupportFragmentManager());
        weatherViewPagerAdapter.addFragment(new ForecastListFragment(), "Weather Info");
        weatherViewPagerAdapter.addFragment(new ChartFragment(), "Demo");
        viewPager.setAdapter(weatherViewPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.myTab);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.example.oop_project.helper_classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.oop_project.PopulationData;
import com.example.oop_project.WeatherData;
import com.example.oop_project.fragments.PopulationDataFragment;
import com.example.oop_project.fragments.WeatherDataFragment;

/// Adapter for the ViewPager2 in CityInformationActivity
public class CityTabPagerAdapter extends FragmentStateAdapter {
    /// Population and weather data
    PopulationData populationData;
    WeatherData weatherData;

    /// Constructor for the adapter
    public CityTabPagerAdapter(@NonNull FragmentActivity fragmentActivity, PopulationData populationData, WeatherData weatherData) {
        super(fragmentActivity);
        this.populationData = populationData;
        this.weatherData = weatherData;
    }

    /// Create the fragment based on the position in the ViewPager2
    /// 0 -> PopulationDataFragment
    /// 1 -> WeatherDataFragment
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new WeatherDataFragment( weatherData );
        }
        return new PopulationDataFragment( populationData );
    }

    @Override
    public int getItemCount() { return 2; }
}

package com.example.oop_project.helper_classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.oop_project.PopulationData;
import com.example.oop_project.WeatherData;
import com.example.oop_project.fragments.PopulationDataFragment;
import com.example.oop_project.fragments.WeatherDataFragment;

public class CityTabPagerAdapter extends FragmentStateAdapter {
    PopulationData populationData;
    WeatherData weatherData;

    public CityTabPagerAdapter(@NonNull FragmentActivity fragmentActivity, PopulationData populationData, WeatherData weatherData) {
        super(fragmentActivity);
        this.populationData = populationData;
        this.weatherData = weatherData;
    }

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

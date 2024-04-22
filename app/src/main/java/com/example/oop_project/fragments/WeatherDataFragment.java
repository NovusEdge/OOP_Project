package com.example.oop_project.fragments;

import static com.example.oop_project.activities.MainActivity.currentCity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.oop_project.R;
import com.example.oop_project.WeatherData;


public class WeatherDataFragment extends Fragment {
    WeatherData weatherData;

    public WeatherDataFragment(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_data, container, false);

        TextView main           = (TextView) view.findViewById(R.id.maindescTextView);
        TextView description    = (TextView) view.findViewById(R.id.longdescTextView);
        TextView tempRange      = (TextView) view.findViewById(R.id.tempRangeTextView);
        TextView humidity       = (TextView) view.findViewById(R.id.humidityTextView);
        TextView meanTempDaily  = (TextView) view.findViewById(R.id.meanDailyTempTextView);
        TextView uviTextView    = (TextView) view.findViewById(R.id.uviTextView);
        TextView precpTextView  = (TextView) view.findViewById(R.id.precpTextView);

        main.setText(String.format("%s: %s", currentCity, weatherData.getMain()));
        description.setText(weatherData.getDescription());
        tempRange.setText(String.format("Temperature range: %lf - %lf", weatherData.getTempRange()[0], weatherData.getTempRange()[1]));
        humidity.setText(String.format("Humidity: %lf", weatherData.getHumidity()));
        meanTempDaily.setText(String.format("Mean daily temperature: %lf", weatherData.getMeanDailyTemp()));
        uviTextView.setText(String.format("UV Index: %lf", weatherData.getUvIndex()));
        precpTextView.setText(String.format("Precipitation: %lf", weatherData.getPrecipitation()));

        return view;
    }
}
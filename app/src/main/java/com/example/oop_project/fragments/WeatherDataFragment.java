package com.example.oop_project.fragments;

import static com.example.oop_project.activities.MainActivity.currentCity;

import android.annotation.SuppressLint;
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

    @SuppressLint("DefaultLocale")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather_data, container, false);

        TextView main           = (TextView) view.findViewById(R.id.maindescTextView);
        TextView tempRange      = (TextView) view.findViewById(R.id.tempRangeTextView);
        TextView humidity       = (TextView) view.findViewById(R.id.humidityTextView);
        TextView meanTempDaily  = (TextView) view.findViewById(R.id.meanDailyTempTextView);
        TextView uviTextView    = (TextView) view.findViewById(R.id.uviTextView);

        String weatherEmoji = "";

        switch (weatherData.getMain().toLowerCase()) {
            case "clear":
                weatherEmoji = "☀️";
                break;
            case "clouds":
                weatherEmoji = "☁️";
                break;
            case "rain":
                weatherEmoji = "🌧️";
                break;
            case "snow":
                weatherEmoji = "❄️";
                break;
            case "thunderstorm":
                weatherEmoji = "⛈️";
                break;
            case "mist":
            case "smoke":
            case "haze":
            case "dust":
            case "fog":
            case "sand":
            case "ash":
                weatherEmoji = "🌫️";
                break;
            case "squall":
            case "tornado":
                weatherEmoji = "🌪️";
                break;
        }


        main.setText(String.format("%s: %s %s", currentCity, weatherData.getMain(), weatherEmoji));

        tempRange.setText(String.format("Temperature range: %f to %f", weatherData.getTempRange()[0], weatherData.getTempRange()[1]));
        humidity.setText(String.format("Humidity: %f", weatherData.getHumidity()));
        meanTempDaily.setText(String.format("Mean daily temperature: %f", weatherData.getMeanDailyTemp()));
        uviTextView.setText(String.format("UV Index: %f", weatherData.getUvIndex()));

        return view;
    }
}
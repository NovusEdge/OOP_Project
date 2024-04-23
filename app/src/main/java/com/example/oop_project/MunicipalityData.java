package com.example.oop_project;

/// Data class to store the population and weather data for a municipality
public class MunicipalityData {
    public PopulationData populationData;
    public WeatherData weatherData;

    public MunicipalityData(PopulationData populationData, WeatherData weatherData) {
        this.populationData = populationData;
        this.weatherData = weatherData;
    }
}

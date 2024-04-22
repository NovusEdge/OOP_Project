package com.example.oop_project;

public class MunicipalityData {
    public PopulationData populationData;
    public WeatherData weatherData;

    public MunicipalityData(PopulationData populationData, WeatherData weatherData) {
        this.populationData = populationData;
        this.weatherData = weatherData;
    }
}

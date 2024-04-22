package com.example.oop_project;

public class WeatherData {
    private String main, description;
    private double[] tempRange;
    private float meanDailyTemp;
    private float uvIndex;
    private float precipitation;
    private float humidity;
    public WeatherData(String main, String description, double[] tempRange, float meanDailyTemp, float uvIndex, float precipitation, float humidity) {
        this.main = main;
        this.description = description;
        this.tempRange = tempRange;
        this.meanDailyTemp = meanDailyTemp;
        this.uvIndex = uvIndex;
        this.precipitation = precipitation;
        this.humidity = humidity;
    }


    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double[] getTempRange() {
        return tempRange;
    }

    public void setTempRange(double[] tempRange) {
        this.tempRange = tempRange;
    }

    public float getMeanDailyTemp() {
        return meanDailyTemp;
    }

    public void setMeanDailyTemp(float meanDailyTemp) {
        this.meanDailyTemp = meanDailyTemp;
    }

    public float getUvIndex() {
        return uvIndex;
    }

    public void setUvIndex(float uvIndex) {
        this.uvIndex = uvIndex;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }
}

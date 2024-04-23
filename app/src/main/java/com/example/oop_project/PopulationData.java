package com.example.oop_project;

/// Data class to store the population data for a city
public class PopulationData {
    private final float workplaceEfficiency;
    private int populationCount;
    private float changeRate;
    private float employementRate;
    private float populationDensity;
    private float citySize;

    public PopulationData(int populationCount, float changeRate, float employementRate, float populationDensity, float citySize, float workplaceEfficiency) {
        this.populationCount = populationCount;
        this.changeRate = changeRate;
        this.employementRate = employementRate;
        this.populationDensity = populationDensity;
        this.citySize = citySize;
        this.workplaceEfficiency = workplaceEfficiency;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///////////////////////// Getters and Setters /////////////////////////////
    ///////////////////////////////////////////////////////////////////////////

    public float getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(float changeRate) {
        this.changeRate = changeRate;
    }

    public int getPopulationCount() {
        return populationCount;
    }

    public void setPopulationCount(int populationCount) {
        this.populationCount = populationCount;
    }

    public float getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(float populationDensity) {
        this.populationDensity = populationDensity;
    }

    public float getEmployementRate() {
        return employementRate;
    }

    public void setEmployementRate(float employementRate) {
        this.employementRate = employementRate;
    }

    public float getCitySize() {
        return citySize;
    }

    public void setCitySize(float citySize) {
        this.citySize = citySize;
    }

    public float getWorkplaceEfficiency() {
        return workplaceEfficiency;
    }
}

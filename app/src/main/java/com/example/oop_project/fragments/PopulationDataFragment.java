package com.example.oop_project.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.oop_project.PopulationData;
import com.example.oop_project.R;


public class PopulationDataFragment extends Fragment {
    PopulationData populationData;
    public PopulationDataFragment(PopulationData populationData) {
        this.populationData = populationData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_population_data, container, false);

        TextView population         = (TextView) view.findViewById(R.id.populationCountTextView);
        TextView populationDensity  = (TextView) view.findViewById(R.id.popoulationDensityTextView);
        TextView changeRate         = (TextView) view.findViewById(R.id.changeRateTextView);
        TextView employmentRate     = (TextView) view.findViewById(R.id.employmentRateTextView);
        TextView citySize           = (TextView) view.findViewById(R.id.citySizeTextView);
        TextView workplaceETextView = (TextView) view.findViewById(R.id.workplaceETextView);

        population.setText(String.format("Population: %d", populationData.getPopulationCount()));
        populationDensity.setText(String.format("Population Density: %f persons per sq. m", populationData.getPopulationDensity()));
        changeRate.setText(String.format("Change Rate: %f", populationData.getChangeRate()));
        employmentRate.setText(String.format("Employment Rate: %f", populationData.getEmployementRate()));
        citySize.setText(String.format("City Size: %f sq. km", populationData.getCitySize()));
        workplaceETextView.setText(String.format("Workplace Efficiency: %f", populationData.getWorkplaceEfficiency()));

        return view;
    }
}
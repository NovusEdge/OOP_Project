package com.example.oop_project.helper_classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.R;

import java.util.ArrayList;

public class CityItemListAdapter extends RecyclerView.Adapter<CityViewHolder> {
    private ArrayList<String> cities;
    Context context;

    public CityItemListAdapter(Context context, ArrayList<String> cities) {
        this.cities = cities;
        this.context = context;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        String aux;
        for(int i = 0; i < cities.size() - 1; i++){
            String currentCity = cities.get(i);
            for(int j = i; j < cities.size(); j++){
                String nextCity = cities.get(j);
                if(currentCity.compareTo(nextCity) > 0){
                    aux = cities.get(i);
                    cities.set(i, cities.get(j));
                    cities.set(j, aux);
                }
            }
        }
        return new CityViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_city_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.cityNameTextView.setText(cities.get(position));
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}

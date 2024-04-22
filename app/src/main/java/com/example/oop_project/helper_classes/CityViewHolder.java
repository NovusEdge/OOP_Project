package com.example.oop_project.helper_classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.R;

public class CityViewHolder extends RecyclerView.ViewHolder {

    TextView cityNameTextView;

    public CityViewHolder(@NonNull View itemView) {
        super(itemView);
        cityNameTextView = itemView.findViewById(R.id.cityName);
    }

    public void bind(String cityName) {
        cityNameTextView.setText(cityName);
    }
}

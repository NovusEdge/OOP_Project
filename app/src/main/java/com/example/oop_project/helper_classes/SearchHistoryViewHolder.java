package com.example.oop_project.helper_classes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.R;

public class SearchHistoryViewHolder extends RecyclerView.ViewHolder {

    /// TextView for the city name
    TextView cityNameTextView;

    /// Constructor
    public SearchHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        cityNameTextView = (TextView) itemView.findViewById(R.id.cityName);
    }
}

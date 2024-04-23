package com.example.oop_project.helper_classes;

import static com.example.oop_project.activities.MainActivity.cityList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.R;

public class SearchHistoryListAdapter extends RecyclerView.Adapter<SearchHistoryViewHolder> {
    Context context;

    public SearchHistoryListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SearchHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchHistoryViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_city_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHistoryViewHolder holder, int position) {
        holder.cityNameTextView.setText(cityList.get(position));
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
}

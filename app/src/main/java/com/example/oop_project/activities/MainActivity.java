package com.example.oop_project.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.DataRetriever;
import com.example.oop_project.MunicipalityData;
import com.example.oop_project.R;
import com.example.oop_project.helper_classes.CityItemListAdapter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static String currentCity;
    public static MunicipalityData currentMunicipalityData;

    public HashMap<String, String> validCities = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // Get UI components:
        Button searchButton = findViewById(R.id.searchButton);
        TextView citySearch = findViewById(R.id.citySearchTextView);
        RecyclerView cityHistoryRecyclerView = findViewById(R.id.cityRecyclerView);

        // Read city search history from file into an arraylist of strings.
        // Each of the names are separated by a newline character.
        Context context = getApplicationContext();
        String cityHistory = readFromCityFile(context);
        ArrayList<String> cityHistoryList = new ArrayList<>();
        Collections.addAll(cityHistoryList, cityHistory.split("\n"));
        CityItemListAdapter cityViewAdapter = new CityItemListAdapter(context, cityHistoryList);
        cityHistoryRecyclerView.setAdapter(cityViewAdapter);

        Thread thread = new Thread(() -> {
            try {
                validCities.putAll((new DataRetriever(context)).GetCityCodes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        thread.start();


        searchButton.setOnClickListener(v -> {
            String city = citySearch.getText().toString();
            if (validCities.containsKey(city)) {
                cityHistoryList.add(city);
                writeToCityFile(String.join("\n", cityHistoryList), context);
                currentCity = city;

                // Start the CityView activity.
                Intent intent = new Intent(MainActivity.this, CityInformationActivity.class);
                startActivity(intent);
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid City")
                        .setMessage("Please enter a valid city name.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    private void writeToCityFile(String data, Context context) {
        // File outputFile = new File(context.getFilesDir(), "city_history.txt");
        try {
            // FileOutputStream stream = new FileOutputStream(outputFile);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("city_history", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e);
        }
    }

    private String readFromCityFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("city_history.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("Error", "File not found: " + e);
        } catch (IOException e) {
            Log.e("Error", "Can not read file: " + e);
        }

        return ret;
    }
}

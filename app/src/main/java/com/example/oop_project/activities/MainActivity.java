package com.example.oop_project.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.DataRetriever;
import com.example.oop_project.R;
import com.example.oop_project.helper_classes.SearchHistoryListAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    /// The file that the city search history is stored in
    public static String CITY_HISTORY_FILE = "city_history.data";

    /// The list of cities that the user has searched for
    public static ArrayList<String> cityList = new ArrayList<>();


    /// The current city that the user is viewing
    public static String currentCity;

    /// A hashmap of valid city names and their corresponding codes
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

        /// Get UI components:
        Button searchButton = findViewById(R.id.searchButton);
        TextView citySearch = findViewById(R.id.citySearchTextView);
        Context context = getApplicationContext();
        RecyclerView cityHistoryRecyclerView = findViewById(R.id.cityRecyclerView);
        cityHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        /// Read city search history from file into an arraylist of strings.
        /// Each of the names are separated by a newline character.
        readFromCityFile(context);

        /// Set up the recycler view for the city search history
        cityHistoryRecyclerView.setAdapter(new SearchHistoryListAdapter(context));

        /// Fetch city codes from the API
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            try {
                validCities.putAll((new DataRetriever(context)).GetCityCodes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        /// Set up the search button to add the city to the list of cities and start the CityView activity. 
        /// If the city is invalid, show an alert dialog.
        searchButton.setOnClickListener(v -> {
            String city = citySearch.getText().toString();
            if (validCities.containsKey(city)) {
                cityList.add(city);
                currentCity = city;

                writeToCityFile(context);
                /// Start the CityView activity
                Intent intent = new Intent(MainActivity.this, CityInformationActivity.class);
                startActivity(intent);
            } else {
                /// Show an alert dialog if the city is invalid
                new AlertDialog.Builder(this)
                        .setTitle("Invalid City")
                        .setMessage("Please enter a valid city name.")
                        .setPositiveButton("OK", null)
                        .show();
            }
        });
    }

    /// Write the city list to the file CITY_HISTORY_FILE
    private void writeToCityFile(Context context) {
        try {
            ObjectOutputStream userWriter = new ObjectOutputStream(context.openFileOutput(CITY_HISTORY_FILE, Context.MODE_APPEND));
            userWriter.writeObject(cityList);
            userWriter.close();
        } catch (IOException e) {
            Log.e("CityHistoryStorage", "Error writing to file", e);
        }
    }

    /// Read the city list from the file CITY_HISTORY_FILE
    private void readFromCityFile(Context context) {
        try {

            /// Read the city list from the file
            ObjectInputStream userReader = new ObjectInputStream(context.openFileInput(CITY_HISTORY_FILE));
            cityList = (ArrayList<String>) userReader.readObject();
            userReader.close();
        } catch (FileNotFoundException e) {
            Log.e("CityHistoryStorage", "File not found", e);
        } catch (IOException e) {
            Log.e("CityHistoryStorage", "Error reading from file", e);
        } catch (ClassNotFoundException e) {
            Log.e("CityHistoryStorage", "Class not found", e);
        }
    }
}

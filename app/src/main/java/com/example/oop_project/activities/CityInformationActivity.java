package com.example.oop_project.activities;

import static com.example.oop_project.activities.MainActivity.currentCity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.oop_project.DataRetriever;
import com.example.oop_project.MunicipalityData;
import com.example.oop_project.R;
import com.example.oop_project.helper_classes.CityTabPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityInformationActivity extends AppCompatActivity {
    public static MunicipalityData currentMunicipalityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_city_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cityInformationView), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// Fetch data
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            DataRetriever dataRetriever = new DataRetriever(getApplicationContext());
            try {
                currentMunicipalityData = dataRetriever.GetMunicipalityData(currentCity);
                if (currentMunicipalityData == null) {
                    Log.e("DATA_FETCH", "Error retrieving data");
                    throw new RuntimeException("Error retrieving data");
                }
                Log.i("DATA_FETCH", "Data retrieved");
            } catch (IOException e) {
                Log.e("DATA_FETCH", "Error retrieving data", e);
                throw new RuntimeException(e);
            }

            /// Update UI
            runOnUiThread(() -> {
                ViewPager2 cityViewPager = (ViewPager2) findViewById(R.id.viewArea);
                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

                CityTabPagerAdapter adapter = new CityTabPagerAdapter(this, currentMunicipalityData.populationData, currentMunicipalityData.weatherData);
                cityViewPager.setAdapter(adapter);

                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        cityViewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) { }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) { }
                });

                cityViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        Objects.requireNonNull(tabLayout.getTabAt(position)).select();
                    }
                });
            });
        });
    }
}
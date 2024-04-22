package com.example.oop_project;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class DataRetriever {
    // !!!!NOTE!!!!
    // NEVER DO THIS IN PRODUCTION CODE. I'M ONLY DOING THIS FOR THE PROJECT BECAUSE I'M NOT ALLOWED TO USE ANYTHING ELSE.
    // ONCE AGAIN THIS IS A SECURITY RISK AND SHOULD NEVER BE DONE IN PRODUCTION CODE.
    private final String WEATHER_API_KEY = "6f519c9d7fbe803f6e7101ed34bb2603";
    private final String WEATHER_API_URL = "https://api.openweathermap.org/data/3.0/onecall?lat=%s&lon=%s&exclude=hourly,minutely&appid=%s";
    private final String GEOLOCATION_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";
    private final String POPULATION_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";

    ObjectMapper objectMapper = new ObjectMapper();

    Context context;

    public DataRetriever(Context context) {
        this.context = context;
    }

    public MunicipalityData GetMunicipalityData(String municipalityName) throws IOException {
        PopulationData populationData = getPopulationData(municipalityName);
        Log.i("DataRetriever", "Population data retrieved");
        WeatherData weatherData = getWeatherData(municipalityName);
        Log.i("DataRetriever", "Weather data retrieved");
        return new MunicipalityData(populationData, weatherData);
    }

    public HashMap<String, String> GetCityCodes() throws IOException {
        JsonNode codes = null;
        JsonNode names = null;

        JsonNode areas = getData(new URL(POPULATION_STATS_API_URL), objectMapper, null, "GET", null);

        Log.i("CityCodes", "City codes retrieved");

        for (JsonNode node : areas.findValue("variables")) {
            if (node.findValue("text").asText().equals("Area")) {
                codes = node.findValue("values");
                names = node.findValue("valueTexts");
            }
        }

        HashMap<String, String> nameToCodes = new HashMap<>();

        for (int i = 0; i < Objects.requireNonNull(names).size(); i++) {
            String name = names.get(i).asText();
            String code = codes.get(i).asText();
            nameToCodes.put(name, code);
        }

        Log.i("CityCodes", "City codes mapped");

        return nameToCodes;
    }

    private WeatherData getWeatherData(String municipalityName) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        URL locationUrl = new URL(String.format(GEOLOCATION_API_URL, municipalityName, WEATHER_API_KEY));
        JsonNode locationJson = getData(locationUrl, objectMapper, null, "GET", null);
        String latitude = locationJson.get(0).get("lat").toString();
        String longitude = locationJson.get(0).get("lon").toString();

        URL weatherUrl = new URL(String.format(WEATHER_API_URL, latitude, longitude, WEATHER_API_KEY));
        JsonNode weatherJson = getData(weatherUrl, objectMapper, null, "GET", null);

        String main = weatherJson.get("current").get("weather").get(0).get("main").asText();
        String description = weatherJson.get("current").get("weather").get(0).get("description").asText();

        double[] tempRange = new double[2];
        tempRange[0] = weatherJson.get("daily").get("temp").get("min").asDouble();
        tempRange[1] = weatherJson.get("daily").get("temp").get("max").asDouble();

        float meanDailyTemp = (float) (tempRange[0] + tempRange[1]) / 2;
        float humidity = weatherJson.get("current").get("humidity").floatValue();

        float uvIndex = weatherJson.get("daily").get("uvi").floatValue();
        float precipitation = weatherJson.get("daily").has("rain") ? weatherJson.get("daily").get("rain").floatValue() : 0;

        Log.i("WeatherData", "Weather data retrieved");

        return new WeatherData(main, description, tempRange, meanDailyTemp, uvIndex, precipitation, humidity);
    }

    private PopulationData getPopulationData(String municipalityName) throws IOException {
        String code = GetCityCodes().get(municipalityName);

        JsonNode jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.population_query));
        JsonNode populationData = getData(new URL(POPULATION_STATS_API_URL), objectMapper, jsonQuery, "POST", code);
        int totalChange = populationData.get("value").get(2).asInt();
        int population = populationData.get("value").get(3).asInt();
        float populationChangeRate = (float) totalChange / population * 100;

        Log.i("PopulationData", "Population data retrieved");

        jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.employment_query));
        String EMPLOYMENT_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        JsonNode employmentData = getData(new URL(EMPLOYMENT_STATS_API_URL), objectMapper, jsonQuery, "POST", code);
        float employmentRate = employmentData.get("value").get(0).floatValue();

        Log.i("EmploymentData", "Employment data retrieved");

        jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.population_density_query));
        String POPULATION_DENSITY_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11ra.px";
        JsonNode populationDensityData = getData(new URL(POPULATION_DENSITY_STATS_API_URL), objectMapper, jsonQuery, "POST", code);
        float citySize = populationDensityData.get("value").get(1).floatValue();
        float populationDensity = populationDensityData.get("value").get(0).floatValue();


        Log.i("PopulationDensityData", "Population density data retrieved");

        jsonQuery = objectMapper.readTree(context.getResources().openRawResource(R.raw.workplace_efficiency_query));
        String WORKPLACE_EFFICIENCY_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
        JsonNode workplaceEfficiencyData = getData(new URL(WORKPLACE_EFFICIENCY_STATS_API_URL), objectMapper, jsonQuery, "POST", code);
        float workplaceEfficiency = workplaceEfficiencyData.get("value").get(0).floatValue();

        Log.i("WorkplaceEfficiencyData", "Workplace efficiency data retrieved");

        return new PopulationData(population, populationChangeRate, employmentRate, populationDensity, citySize, workplaceEfficiency);
    }

    private JsonNode getData(URL url, ObjectMapper objectMapper, JsonNode jsonQuery, String queryType, String cityCode) throws IOException {
        HttpURLConnection con;

        if (queryType.equals("GET")) {
            // con = connectToAPIAndSendGetRequest(url);
            return objectMapper.readTree(url);
        } else if (queryType.equals("POST")) {
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").removeAll();
            ((ObjectNode) jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(cityCode);
            con = connectToAPIAndSendPostRequest(url, objectMapper, jsonQuery);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                return objectMapper.readTree(response.toString());
            } catch (IOException e) {
                Log.e("DATA_FETCH", "Error reading response", e);
                throw new IOException("Error reading response", e);
            } finally {
                con.disconnect();
            }
        } else {
            Log.e("Error", "Invalid query type");
            throw new IOException("Invalid query type");
        }
    }

    private HttpURLConnection connectToAPIAndSendPostRequest(URL url, ObjectMapper objectMapper, JsonNode jsonQuery)
            throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);
        }
        return con;
    }

    private HttpURLConnection connectToAPIAndSendGetRequest(URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        return con;
    }
}

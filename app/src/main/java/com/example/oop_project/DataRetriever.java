package com.example.oop_project;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public class DataRetriever {
    /// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! NOTE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    /// NEVER DO THIS IN PRODUCTION CODE. I'M ONLY DOING THIS FOR THE PROJECT 
    /// BECAUSE I'M NOT ALLOWED TO USE ANYTHING ELSE. THIS IS A SECURITY RISK 
    /// AND SHOULD NEVER BE DONE IN PRODUCTION CODE.
    /// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private final String WEATHER_API_KEY = "6f519c9d7fbe803f6e7101ed34bb2603";

    /// API URLs for fetching data
    private final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&exclude=hourly,minutely&appid=%s";
    private final String GEOLOCATION_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s";
    private final String POPULATION_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px";
    private final String UVI_API_URL = "https://currentuvindex.com/api/v1/uvi?latitude=%s&longitude=%s";

    /// Jackson object mapper for parsing JSON
    private static ObjectMapper objectMapper = new ObjectMapper();

    /// Android context for reading resources
    Context context; 

    public DataRetriever(Context context) {
        this.context = context;
    }

    /// Fetches the population and weather data for a given municipality
    public MunicipalityData GetMunicipalityData(String municipalityName) throws IOException {
        PopulationData populationData = getPopulationData(municipalityName);
        Log.i("DataRetriever", "Population data retrieved");
        WeatherData weatherData = getWeatherData(municipalityName);
        Log.i("DataRetriever", "Weather data retrieved");
        return new MunicipalityData(populationData, weatherData);
    }

    /// Fetches the city codes for all municipalities in Finland, and maps them to their respective names into a hashmap
    public HashMap<String, String> GetCityCodes() throws IOException {
        JsonNode codes = null;
        JsonNode names = null;

        JsonNode areas = getData(new URL(POPULATION_STATS_API_URL));

        Log.i("CityCodes", "City codes retrieved");

        assert areas != null;
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

    /// Fetches the weather data for a given municipality using the OpenWeatherMap and CurrentUVIndex APIs
    private WeatherData getWeatherData(String municipalityName) throws IOException {
        URL locationUrl = new URL(String.format(GEOLOCATION_API_URL, municipalityName, WEATHER_API_KEY));
        JsonNode locationJson = getData(locationUrl);
        String latitude = locationJson.get(0).get("lat").toString();
        String longitude = locationJson.get(0).get("lon").toString();

        URL weatherUrl = new URL(String.format(WEATHER_API_URL, latitude, longitude, WEATHER_API_KEY));
        JsonNode weatherJson = getData(weatherUrl);

        String main = weatherJson.get("weather").get(0).get("main").asText();
        String description = weatherJson.get("weather").get(0).get("description").asText();

        double[] tempRange = new double[2];
        tempRange[0] = weatherJson.get("main").get("temp_min").asDouble() - 273.15;
        tempRange[1] = weatherJson.get("main").get("temp_max").asDouble() - 273.15;

        float meanDailyTemp = (float) (tempRange[0] + tempRange[1]) / 2;
        float humidity = weatherJson.get("main").get("humidity").floatValue();


        URL uviUrl = new URL(String.format(UVI_API_URL, latitude, longitude));
        JsonNode uviJson = getData(uviUrl);
        float uvIndex = uviJson.get("now").get("uvi").floatValue();

        Log.i("WeatherData", "Weather data retrieved");

        return new WeatherData(main, description, tempRange, meanDailyTemp, uvIndex, humidity);
    }

    /// Fetches the population data for a given municipality using the PXData API from Statistics Finland
    private PopulationData getPopulationData(String municipalityName) throws IOException {
        String code = GetCityCodes().get(municipalityName);

        JsonNode populationData = getData(new URL(POPULATION_STATS_API_URL), context.getResources().openRawResource(R.raw.population_query), code);
        int totalChange = populationData.get("value").get(2).asInt();
        int population = populationData.get("value").get(3).asInt();
        float populationChangeRate = (float) totalChange / population * 100;

        Log.i("Population", "Population number retrieved");

        String EMPLOYMENT_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_115x.px";
        JsonNode employmentData = getData(new URL(EMPLOYMENT_STATS_API_URL), context.getResources().openRawResource(R.raw.employment_query), code);
        float employmentRate = employmentData.get("value").get(0).floatValue();

        Log.i("EmploymentData", "Employment data retrieved");

        String POPULATION_DENSITY_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/vaerak/statfin_vaerak_pxt_11ra.px";
        JsonNode populationDensityData = getData(new URL(POPULATION_DENSITY_STATS_API_URL), context.getResources().openRawResource(R.raw.population_density_query), code);
        float citySize = populationDensityData.get("value").get(1).floatValue();
        float populationDensity = populationDensityData.get("value").get(0).floatValue();

        Log.i("PopulationDensityData", "Population density data retrieved");

        String WORKPLACE_EFFICIENCY_STATS_API_URL = "https://pxdata.stat.fi:443/PxWeb/api/v1/en/StatFin/tyokay/statfin_tyokay_pxt_125s.px";
        JsonNode workplaceEfficiencyData = getData(new URL(WORKPLACE_EFFICIENCY_STATS_API_URL), context.getResources().openRawResource(R.raw.workplace_efficiency_query), code);
        float workplaceEfficiency = workplaceEfficiencyData.get("value").get(0).floatValue();

        Log.i("WorkplaceEfficiencyData", "Workplace efficiency data retrieved");

        return new PopulationData(population, populationChangeRate, employmentRate, populationDensity, citySize, workplaceEfficiency);
    }

    /// Fetches data from a given URL. This is a method overload for fetching data with a GET request
    private static JsonNode getData(URL locationUrl){
        try {
            return objectMapper.readTree(locationUrl);
        } catch(MalformedURLException e){
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /// Fetches data from a given URL. This is a method overload for fetching data with a POST request
    /// The query for fetching data from a single municipality is stored in query.json
    private static JsonNode getData(URL sourceURL, InputStream query, String code){
        try {
            // The query for fetching data from a single municipality is stored in query.json
            JsonNode jsonQuery = objectMapper.readTree(query);
            // Let's replace the municipality code in the query with the municipality that the user gave
            // as input
            ((ObjectNode)jsonQuery.findValue("query").get(0).get("selection")).putArray("values").add(code);

            HttpURLConnection con = connectToAPIAndSendPostRequest(objectMapper, jsonQuery, sourceURL);

            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonNode municipalityData = objectMapper.readTree(response.toString());

                return municipalityData;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    /// Connects to an API and sends a POST request
    private static HttpURLConnection connectToAPIAndSendPostRequest(ObjectMapper objectMapper, JsonNode jsonQuery, URL url)
            throws MalformedURLException, IOException, ProtocolException, JsonProcessingException {

        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = objectMapper.writeValueAsBytes(jsonQuery);
            os.write(input, 0, input.length);
        }
        return con;
    }
}

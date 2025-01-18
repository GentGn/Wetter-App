package at.fhtw.wetter_app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Diese Klasse verwendet die Jackson-Bibliothek, um Wetterdaten von der WeatherAPI abzurufen und zu verarbeiten.
 */
public class WeatherServiceAPI {

    private static final String API_KEY = "d82d4b74e8b047ecaf5174637240212";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static String buildUrl(String city) {
        return BASE_URL + "?key=" + API_KEY + "&q=" + city + "&lang=de";
    }

    private static String fetchResponse(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return new String(connection.getInputStream().readAllBytes());
        } else {
            throw new IOException("Fehler beim Abrufen der Wetterdaten: " + connection.getResponseCode());
        }
    }

    public static WeatherData getWeatherData(String city) throws IOException {
        String response = fetchResponse(buildUrl(city));
        JsonNode rootNode = OBJECT_MAPPER.readTree(response);

        String condition = rootNode.path("current").path("condition").path("text").asText("Unbekannt");
        double temperature = rootNode.path("current").path("temp_c").asDouble(Double.NaN);
        int humidity = rootNode.path("current").path("humidity").asInt(-1);
        int uvIndex = rootNode.path("current").path("uv").asInt(-1);
        double windKph = rootNode.path("current").path("wind_kph").asDouble(Double.NaN);
        String windDir = rootNode.path("current").path("wind_dir").asText("Unbekannt");
        String cityName = rootNode.path("location").path("name").asText();

        return new WeatherData(condition, temperature, humidity, uvIndex, windKph, windDir, cityName);
    }

    public static void main(String[] args) {
        try {
            WeatherData weatherData = getWeatherData("Wien");
            System.out.println(weatherData);
        } catch (IOException e) {
            System.err.println("Fehler beim Abrufen der Wetterdaten: " + e.getMessage());
        }
    }

    /**
     * Findet die Stadt mit der höchsten Temperatur aus einer Liste von 100 Städten.
     *
     * @param cities Eine Liste von Städten, für die die Wetterdaten abgerufen werden sollen.
     * @return Der Name der Stadt mit der höchsten Temperatur.
     */
    public static String getCityWithHighestTemperature(List<String> cities) {
        String hottestCity = "";
        double highestTemp = Double.NEGATIVE_INFINITY;

        for (String city : cities) {
            try {
                // Fetch the weather data for the current city
                WeatherData weatherData = getWeatherData(city);

                // Compare the temperature to the highest found so far
                if (weatherData.getTemperature() > highestTemp) {
                    highestTemp = weatherData.getTemperature();
                    hottestCity = weatherData.getCityName();
                }
            } catch (IOException e) {
                System.err.println("Fehler beim Abrufen der Wetterdaten für " + city + ": " + e.getMessage());
            }
        }
        return hottestCity;
    }
}
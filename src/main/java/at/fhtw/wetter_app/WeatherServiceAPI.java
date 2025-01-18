package at.fhtw.wetter_app;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Diese Klasse bietet Methoden zum Abrufen von Wetterdaten über die WeatherAPI.
 */
public class WeatherServiceAPI {

    private static final String API_KEY = "d82d4b74e8b047ecaf5174637240212";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Baut die API-URL für eine bestimmte Stadt.
     *
     * @param city Der Name der Stadt.
     * @return Die vollständige API-URL.
     */
    private static String buildUrl(String city) {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        return BASE_URL + "?key=" + API_KEY + "&q=" + encodedCity + "&lang=de";
    }

    /**
     * Ruft die API-Antwort für eine bestimmte URL ab.
     *
     * @param urlString Die API-URL.
     * @return Die API-Antwort als String.
     * @throws IOException Falls ein Fehler beim Abrufen der Daten auftritt.
     */
    private static String fetchResponse(String urlString) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        } else {
            throw new IOException("Fehler beim Abrufen der Wetterdaten: " + connection.getResponseCode());
        }
    }

    /**
     * Ruft die Wetterdaten für eine bestimmte Stadt ab.
     *
     * @param city Der Name der Stadt.
     * @return Ein WeatherData-Objekt mit den abgerufenen Daten.
     * @throws IOException Falls ein Fehler beim Abrufen oder Verarbeiten der Daten auftritt.
     */
    public static WeatherData getWeatherData(String city) throws IOException {
        String url = buildUrl(city);
        String response = fetchResponse(url);
        JsonNode rootNode = OBJECT_MAPPER.readTree(response);

        String condition = rootNode.path("current").path("condition").path("text").asText("Unbekannt");
        double temperature = rootNode.path("current").path("temp_c").asDouble(Double.NaN);
        int humidity = rootNode.path("current").path("humidity").asInt(-1);
        double uvIndex = rootNode.path("current").path("uv").asDouble(Double.NaN);
        double windKph = rootNode.path("current").path("wind_kph").asDouble(Double.NaN);
        String windDir = rootNode.path("current").path("wind_dir").asText("Unbekannt");
        String cityName = rootNode.path("location").path("name").asText();

        return new WeatherData(condition, temperature, humidity, uvIndex, windKph, windDir, cityName);
    }

    /**
     * Findet die Stadt mit der höchsten Temperatur aus einer Liste von Städten.
     *
     * @param cities Eine Liste von Städtenamen.
     * @return Der Name der Stadt mit der höchsten Temperatur.
     * @throws IOException Falls keine gültigen Wetterdaten gefunden werden.
     */
    public static String getCityWithHighestTemperature(List<String> cities) throws IOException {
        String hottestCity = "";
        double highestTemp = Double.NEGATIVE_INFINITY;

        for (String city : cities) {
            try {
                WeatherData weatherData = getWeatherData(city);
                if (weatherData.getTemperature() > highestTemp) {
                    highestTemp = weatherData.getTemperature();
                    hottestCity = weatherData.getCityName();
                }
            } catch (IOException e) {
                // Falls es einen Fehler bei einer einzelnen Stadt gibt, einfach überspringen
                System.err.println("Fehler beim Abrufen der Wetterdaten für " + city + ": " + e.getMessage());
            }
        }

        if (hottestCity.isEmpty()) {
            throw new IOException("Keine gültigen Wetterdaten gefunden.");
        }

        return hottestCity;
    }
}

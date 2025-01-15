package at.fhtw.wetter_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherServiceAPI {

    private static final String API_KEY_MUHAMMAD = "d82d4b74e8b047ecaf5174637240212";

    // Methode zur Abfrage der Wetterdaten der API
    public static String getWeatherData(String city) throws IOException {
        String urlString = "https://api.weatherapi.com/v1/current.json?key=" + API_KEY_MUHAMMAD + "&q=" + city;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            return response.toString();
        }
    }

    // Methode zum Extrahieren des Stadtnamens aus der JSON-Antwort
    public static String parseCityName(String response) {
        int nameIndex = response.indexOf("\"name\":\"");
        if (nameIndex == -1) return "Unbekannt";
        int startIndex = nameIndex + 8; // Länge von "\"name\":\""
        int endIndex = response.indexOf("\"", startIndex);
        return response.substring(startIndex, endIndex);
    }

    // Methode zum Extrahieren der Temperatur aus der JSON-Antwort
    public static double parseTemperature(String response) {
        int tempIndex = response.indexOf("\"temp_c\":");
        if (tempIndex == -1) return Double.NaN;
        int startIndex = tempIndex + 9; // Länge von "\"temp_c\":"
        int endIndex = response.indexOf(",", startIndex);
        if (endIndex == -1) endIndex = response.indexOf("}", startIndex);
        String tempString = response.substring(startIndex, endIndex);
        return Double.parseDouble(tempString);
    }
}

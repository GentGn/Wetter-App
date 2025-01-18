package at.fhtw.wetter_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class WeatherServiceAPI {

    private static final String API_KEY = "d82d4b74e8b047ecaf5174637240212";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";

    // Methode zur Erstellung der vollständigen URL für eine Stadt
    private static String buildUrl(String city) {
        return BASE_URL + "?key=" + API_KEY + "&q=" + city + "&lang=de";
    }

    // Methode zur Initialisierung der Verbindung
    private static HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    // Methode zum Abrufen der Rohdaten aus der API
    private static String fetchResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();
        }
    }

    // Hauptmethode: Wetterdaten für eine Stadt abrufen
    public static String getWeatherData(String city) throws IOException {
        String urlString = buildUrl(city);
        HttpURLConnection connection = createConnection(urlString);
        return fetchResponse(connection);
    }

    // JSON-Parsing: Stadtnamen extrahieren
    public static String parseCityName(String response) {
        return extractJsonField(response, "\"name\":\"");
    }

    // JSON-Parsing: Temperatur extrahieren
    public static double parseTemperature(String response) {
        String tempString = extractJsonField(response, "\"temp_c\":");
        if (tempString.isEmpty()) return Double.NaN;
        // Validieren, dass der String eine gültige Zahl ist
        tempString = tempString.trim();
        if (tempString.matches("^-?\\d+(\\.\\d+)?$")) {
            return Double.parseDouble(tempString);
        } else {
            System.err.println("Ungültiger Temperaturwert: " + tempString);
            return Double.NaN;
        }
    }

    public static String parseWeatherCondition(String response) {
        // Sucht nach dem Schlüssel "text" innerhalb des "condition"-Objekts
        String condition = extractJsonField1(response, "\"condition\":{\"text\":\"");
        // Entfernt abschließend das schließende Anführungszeichen und die geschweifte Klammer
        if (condition != null && condition.contains("\"")) {
            condition = condition.substring(0, condition.indexOf("\""));
        }
        if (condition != null && !condition.isEmpty()) {
            // Den ersten Buchstaben großschreiben und den Rest unverändert lassen
            condition = condition.substring(0, 1).toUpperCase() + condition.substring(1);
        }
        return condition != null ? condition : "Unbekannt";
    }


    public static int parseHumidity(String response) {
        String humidityString = extractJsonField(response, "\"humidity\":");
        try {
            return Integer.parseInt(humidityString.trim());
        } catch (NumberFormatException e) {
            System.err.println("Ungültiger Luftfeuchtigkeitswert: " + humidityString);
            return -1; // Rückgabewert für ungültige Luftfeuchtigkeit
        }
    }

    public static double parseWindSpeed(String response) {
        String windSpeedString = extractJsonField(response, "\"wind_kph\":");
        try {
            return Double.parseDouble(windSpeedString.trim());
        } catch (NumberFormatException e) {
            System.err.println("Ungültiger Windgeschwindigkeitswert: " + windSpeedString);
            return Double.NaN; // Rückgabewert für ungültige Windgeschwindigkeit
        }
    }

    public static String extractJsonField1(String response, String field) {
        int startIndex = response.indexOf(field);
        if (startIndex == -1) {
            return null;  // Wenn das Feld nicht gefunden wurde
        }
        startIndex += field.length();  // Index nach dem Feldnamen
        int endIndex = response.indexOf("\"", startIndex);  // Suche nach dem schließenden Anführungszeichen
        if (endIndex == -1) {
            return null;  // Wenn kein schließendes Anführungszeichen gefunden wurde
        }
        return response.substring(startIndex, endIndex);  // Extrahiert den Wert
    }


    // Hilfsmethode für JSON-Feldextraktion
    private static String extractJsonField(String response, String fieldName) {
        int fieldIndex = response.indexOf(fieldName);
        if (fieldIndex == -1) {
            return "";
        }
        int startIndex = response.indexOf(":", fieldIndex) + 1; // Start nach dem Doppelpunkt
        int endIndex = response.indexOf(",", startIndex); // Ende beim nächsten Komma
        if (endIndex == -1) {
            endIndex = response.indexOf("}", startIndex); // Alternative: Ende beim nächsten geschlossenen Tag
        }
        return response.substring(startIndex, endIndex).replace("\"", "").trim();
    }


    // Methode: Stadt mit der höchsten Temperatur aus einer Liste von 100 größten Städten finden
    public static String getCityWithHighestTemperature(List<String> cities) {
        String hottestCity = "";
        double highestTemp = Double.NEGATIVE_INFINITY;

        for (String city : cities) {
            try {
                String response = getWeatherData(city);
                double temperature = parseTemperature(response);
                if (temperature > highestTemp) {
                    highestTemp = temperature;
                    hottestCity = city;
                }
            } catch (IOException e) {
                System.out.println("Fehler beim Abrufen der Wetterdaten für " + city + ": " + e.getMessage());
            }
        }
        return hottestCity;
    }

}

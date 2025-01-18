package at.fhtw.wetter_app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Diese Klasse stellt Methoden zur Verfügung, um Wetterdaten von der WeatherAPI abzurufen und zu verarbeiten.
 * Sie ermöglicht das Abrufen von Wetterdaten wie Temperatur, Luftfeuchtigkeit, Windgeschwindigkeit und Wetterbedingungen
 * für eine bestimmte Stadt.
 */
public class WeatherServiceAPI {

    private static final String API_KEY = "d82d4b74e8b047ecaf5174637240212";
    private static final String BASE_URL = "https://api.weatherapi.com/v1/current.json";

    /**
     * Erzeugt eine vollständige URL zur WeatherAPI für eine gegebene Stadt.
     *
     * @param city Der Name der Stadt, für die die Wetterdaten abgerufen werden sollen.
     * @return Eine URL, die für die Anfrage an die WeatherAPI verwendet werden kann.
     */
    private static String buildUrl(String city) {
        return BASE_URL + "?key=" + API_KEY + "&q=" + city + "&lang=de";
    }

    /**
     * Stellt eine HTTP-Verbindung zu der angegebenen URL her.
     *
     * @param urlString Die URL, zu der die Verbindung aufgebaut werden soll.
     * @return Eine HttpURLConnection, die für die API-Anfrage verwendet werden kann.
     * @throws IOException Wenn ein Fehler bei der Verbindung zur URL auftritt.
     */
    private static HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        return connection;
    }

    /**
     * Ruft die Rohdaten von der API ab, die durch die angegebene HttpURLConnection repräsentiert wird.
     *
     * @param connection Die HttpURLConnection, die mit der API verbunden ist.
     * @return Die Antwort der API als String.
     * @throws IOException Wenn ein Fehler beim Abrufen der Antwort auftritt.
     */
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

    /**
     * Ruft die Wetterdaten für eine bestimmte Stadt ab.
     * Diese Methode verbindet sich mit der API und gibt die Rohdaten der Antwort als String zurück.
     *
     * @param city Der Name der Stadt, für die die Wetterdaten abgerufen werden sollen.
     * @return Die Wetterdaten im JSON-Format als String.
     * @throws IOException Wenn ein Fehler beim Abrufen der Wetterdaten auftritt.
     */
    public static String getWeatherData(String city) throws IOException {
        // Der Städtename wird in die URL eingefügt.
        String urlString = buildUrl(city);
        HttpURLConnection connection = createConnection(urlString);
        return fetchResponse(connection);
    }

    /**
     * Extrahiert den Städtenamen aus der API-Antwort im JSON-Format.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @return Der Städtename als String.
     */
    public static String parseCityName(String response) {
        return extractJsonField(response, "\"name\":\"");
    }

    /**
     * Extrahiert die Temperatur aus der API-Antwort im JSON-Format.
     * Falls der Wert ungültig ist, wird `Double.NaN` zurückgegeben.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @return Die Temperatur als `double`, oder `Double.NaN`, wenn der Wert ungültig ist.
     */
    public static double parseTemperature(String response) {

        // Ruft den Temperaturwert aus der JSON-Antwort ab, indem das Feld "temp_c" extrahiert wird
        String tempString = extractJsonField(response, "\"temp_c\":");

        // Wenn der extrahierte Wert leer ist, gibt die Methode NaN (Not a Number) zurück, was auf einen Fehler hinweist.
        if (tempString.isEmpty()) return Double.NaN;
        // Validieren, dass der String eine gültige Zahl ist
        tempString = tempString.trim();
        if (tempString.matches("^-?\\d+(\\.\\d+)?$")) { //Der reguläre Ausdruck ^-?\\d+(\\.\\d+)?$ überprüft,
                                                                // ob eine Zeichenkette eine gültige Zahl ist,
                                                                // die optional eine Dezimalstelle enthalten kann.
                                                                // Er validiert sowohl ganzzahlige als auch dezimalisierte Zahlen,
                                                                // sowohl positive als auch negative.
            return Double.parseDouble(tempString);
        } else {
            // Wenn der Wert nicht gültig ist, wird eine Fehlermeldung ausgegeben und NaN zurückgegeben
            System.err.println("Ungültiger Temperaturwert: " + tempString);
            return Double.NaN;
        }
    }

    /**
     * Extrahiert die Wetterbedingungen aus der API-Antwort im JSON-Format.
     * Der erste Buchstabe der Bedingung wird in Großbuchstaben umgewandelt.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @return Die Wetterbedingungen als String.
     */
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

    /**
     * Extrahiert die Luftfeuchtigkeit aus der API-Antwort im JSON-Format.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @return Die Luftfeuchtigkeit als `int`, oder -1, wenn der Wert ungültig ist.
     */
    public static int parseHumidity(String response) {
        // Extrahieren des Werts für die Luftfeuchtigkeit aus der JSON-Antwort
        String humidityString = extractJsonField(response, "\"humidity\":");
        try {
            // Versuchen, den extrahierten String in eine ganze Zahl (int) umzuwandeln
            return Integer.parseInt(humidityString.trim());
        } catch (NumberFormatException e) {
            // Falls der String keine gültige Zahl enthält (z.B. "null" oder ein ungültiges Format)
            System.err.println("Ungültiger Luftfeuchtigkeitswert: " + humidityString);
            // Rückgabe eines speziellen Wertes (-1) für ungültige oder nicht parsbare Werte
            return -1; // Rückgabewert für ungültige Luftfeuchtigkeit
        }
    }

    /**
     * Extrahiert die Windgeschwindigkeit aus der API-Antwort im JSON-Format.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @return Die Windgeschwindigkeit als `double`, oder `Double.NaN`, wenn der Wert ungültig ist.
     */
    public static double parseWindSpeed(String response) {
        // Extrahieren des Werts für die Windgeschwindigkeit aus der JSON-Antwort
        String windSpeedString = extractJsonField(response, "\"wind_kph\":");
        try {
            // Versuchen, den extrahierten String in einen double-Wert (Gleitkommazahl) umzuwandeln
            return Double.parseDouble(windSpeedString.trim());
        } catch (NumberFormatException e) {
            // Falls der String keine gültige Zahl enthält (z.B. "null" oder ein ungültiges Format)
            System.err.println("Ungültiger Windgeschwindigkeitswert: " + windSpeedString);
            return Double.NaN; // Rückgabewert für ungültige Windgeschwindigkeit
        }
    }

    /**
     * Hilfsmethode zur Extraktion eines bestimmten JSON-Feldes aus der API-Antwort. Diese Methode wird nur in
     * der parsingMethoden parseWeatherCondition verwendet.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @param field Das Feld, das extrahiert werden soll.
     * @return Der extrahierte Wert des Feldes als String, oder `null`, wenn das Feld nicht gefunden wurde.
     */
    public static String extractJsonField1(String response, String field) {
        // Suche nach dem Index des Anfangs des angegebenen Feldes im JSON-String
        int startIndex = response.indexOf(field);

        if (startIndex == -1) {
            return null;  // Wenn das Feld nicht gefunden wurde
        }
        // Bewege den Start-Index nach dem Feldnamen, um auf den Wert zu zeigen
        startIndex += field.length();

        // Suche nach dem Index des ersten Anführungszeichens nach dem Start-Index
        int endIndex = response.indexOf("\"", startIndex);

        if (endIndex == -1) {
            return null;  // Wenn kein schließendes Anführungszeichen gefunden wurde
        }
        // Extrahiert den Wert des Feldes, indem ein Substring zwischen den gefundenen Indizes erstellt wird
        return response.substring(startIndex, endIndex);
    }


    /**
     * Hilfsmethode zur Extraktion eines bestimmten JSON-Feldes aus der API-Antwort. Diese Methode wird in allen
     * parsingMethoden außer parseWeatherCondition verwendet.
     *
     * @param response Die API-Antwort im JSON-Format.
     * @param fieldName Der Name des Feldes, dessen Wert extrahiert werden soll.
     * @return Der extrahierte Wert des Feldes als String, oder ein leerer String, wenn das Feld nicht gefunden wurde.
     */
    private static String extractJsonField(String response, String fieldName) {
        // Sucht nach dem Index des angegebenen Feldes (fieldName) im JSON-String
        int fieldIndex = response.indexOf(fieldName);

        // Wenn das Feld nicht gefunden wird, gib einen leeren String zurück
        if (fieldIndex == -1) {
            return "";  // Das Feld wurde nicht gefunden
        }

        // Der Start-Index ist nach dem Doppelpunkt, der den Wert vom Feld trennt
        int startIndex = response.indexOf(":", fieldIndex) + 1; // Start nach dem Doppelpunkt

        // Sucht nach dem nächsten Komma, um das Ende des Wertes zu finden
        int endIndex = response.indexOf(",", startIndex); // Ende beim nächsten Komma

        // Falls kein Komma gefunden wird, sucht nach dem geschlossenen geschweiften Klammer "}", um das Ende zu finden
        if (endIndex == -1) {
            endIndex = response.indexOf("}", startIndex); // Alternative: Ende beim nächsten geschlossenen Tag
        }

        // Extrahiert den Wert des Feldes zwischen dem Start- und End-Index, entfernt Anführungszeichen und Leerzeichen
        return response.substring(startIndex, endIndex).replace("\"", "").trim();
    }



    /**
     * Findet die Stadt mit der höchsten Temperatur aus einer Liste von 100 Städten.
     *
     * @param cities Eine Liste von Städten, für die die Wetterdaten abgerufen werden sollen.
     * @return Der Name der Stadt mit der höchsten Temperatur.
     */
    public static String getCityWithHighestTemperature(List<String> cities) {
        // Variable zum Speichern der heißesten Stadt (initialisiert mit einem leeren String)
        String hottestCity = "";

        // Variable zum Speichern der höchsten Temperatur, initialisiert mit dem kleinsten möglichen Wert.
        // Jeder tatsächliche Wert für highestTemp wird größer sein als Double.NEGATIVE_INFINITY und somit
        // den Verglich ermöglichen.
        double highestTemp = Double.NEGATIVE_INFINITY;

        // Durchlaufe die Liste der Städte, um die Temperatur für jede Stadt abzufragen
        for (String city : cities) {
            try {
                // Hole die Wetterdaten der aktuellen Stadt
                String response = getWeatherData(city);

                // Extrahiere die Temperatur aus der Antwort
                double temperature = parseTemperature(response);

                // Vergleiche die extrahierte Temperatur mit der höchsten bisher gefundenen Temperatur
                if (temperature > highestTemp) {
                    highestTemp = temperature; // Setze die höchste Temperatur auf den aktuellen Wert
                    hottestCity = city; // Setze die Stadt mit der höchsten Temperatur als heißeste Stadt
                }
            } catch (IOException e) {
                // Falls ein Fehler beim Abrufen der Wetterdaten auftritt, gebe eine Fehlermeldung aus
                System.out.println("Fehler beim Abrufen der Wetterdaten für " + city + ": " + e.getMessage());
            }
        }

        // Rückgabe der Stadt mit der höchsten Temperatur
        return hottestCity;
    }

}

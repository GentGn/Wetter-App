package at.fhtw.wetter_app;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Diese Klasse stellt Methoden zum Speichern und Laden von Wetterdaten (WeatherData-Objekten) in/aus einer JSON-Datei bereit.
 */
public class WeatherDataIO {

    private static final String FILE_NAME = "weather_data.json";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Speichert ein WeatherData-Objekt in eine JSON-Datei.
     *
     * @param weatherData Das zu speichernde WeatherData-Objekt.
     */
    public static void saveWeatherData(WeatherData weatherData) {
        try {
            OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_NAME), weatherData);
            System.out.println("Wetterdaten erfolgreich in " + FILE_NAME + " gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Wetterdaten: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lädt ein WeatherData-Objekt aus der JSON-Datei (falls vorhanden).
     *
     * @return Das geladene WeatherData-Objekt oder null, wenn keine Daten vorhanden sind oder ein Fehler auftritt.
     */
    public static WeatherData loadWeatherData() {
        try {
            File file = new File(FILE_NAME);
            if (file.exists()) {
                return OBJECT_MAPPER.readValue(file, WeatherData.class);
            } else {
                System.out.println("Keine gespeicherten Wetterdaten gefunden.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der Wetterdaten: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

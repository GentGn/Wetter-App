package at.fhtw.wetter_app.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse enthält Methoden zur Kodierung von Städtenamen in das URL-Encoding-Format.
 * Sie verwendet die UTF-8-Kodierung, um Sonderzeichen in Städtenamen für die Verwendung in URLs zu encodieren.
 */
public class CityEncoder {

    /**
     * Kodiert eine Liste von Städtenamen in das URL-Encoding-Format.
     * Diese Methode verwendet UTF-8, um alle Sonderzeichen in den Städtenamen zu kodieren.
     *
     * @param cities Eine Liste von Städtenamen, die kodiert werden sollen.
     * @return Eine Liste der kodierten Städtenamen. Falls ein Fehler auftritt, werden nur die erfolgreich kodierten Städtenamen in der Liste enthalten sein.
     */
    public static List<String> encodeCityNames(List<String> cities) {
        List<String> encodedCities = new ArrayList<>();
        for (String city : cities) {
            try {
                String encodedCity = URLEncoder.encode(city, "UTF-8");
                encodedCities.add(encodedCity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  // Handle exception for unsupported encoding
            }
        }
        return encodedCities;
    }

    /**
     * Kodiert einen einzelnen Städtenamen in das URL-Encoding-Format.
     * Diese Methode verwendet UTF-8, um Sonderzeichen im Städtenamen zu kodieren.
     *
     * @param city Der Stadtname, der kodiert werden soll.
     * @return Der kodierte Städtename im URL-Encoding-Format oder null, wenn ein Fehler bei der Kodierung auftritt.
     */
    public static String encodeCityName(String city) {
        try {
            return URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  // Handle exception for unsupported encoding
            return null;          // Return null if encoding fails
        }
    }
}

package at.fhtw.wetter_app.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CityEncoder {

    // Method to encode city names
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

    public static String encodeCityName(String city) {
        try {
            return URLEncoder.encode(city, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  // Handle exception for unsupported encoding
            return null;          // Return null if encoding fails
        }
    }
}

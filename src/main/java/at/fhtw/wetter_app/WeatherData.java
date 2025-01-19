package at.fhtw.wetter_app;

/**
 * Repräsentiert Wetterdaten für eine bestimmte Stadt.
 * Enthält Attribute wie Wetterzustand, Temperatur, Luftfeuchtigkeit und Windinformationen.
 */
public class WeatherData {

    private String condition;  // Wetterzustand (z. B. Sonnig, Wolkig)
    private double temperature; // Temperatur in Celsius
    private int humidity;       // Luftfeuchtigkeit in Prozent
    private double uvIndex;     // UV-Index
    private double windKph;     // Windgeschwindigkeit in km/h
    private String windDir;     // Windrichtung
    private String cityName;    // Name der Stadt

    /**
     * Standardkonstruktor (erforderlich für Jackson).
     */
    public WeatherData() {
    }

    /**
     * Konstruktor für das WeatherData-Objekt.
     *
     * @param condition   Wetterzustand (z. B. "Sonnig").
     * @param temperature Temperatur in Celsius.
     * @param humidity    Luftfeuchtigkeit in Prozent.
     * @param uvIndex     UV-Index.
     * @param windKph     Windgeschwindigkeit in km/h.
     * @param windDir     Windrichtung.
     * @param cityName    Name der Stadt.
     */
    public WeatherData(String condition, double temperature, int humidity, double uvIndex, double windKph, String windDir, String cityName) {
        this.condition = condition;
        this.temperature = temperature;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.windKph = windKph;
        this.windDir = windDir;
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "Wetterdaten für " + cityName + ":\n" +
                "Bedingung: " + condition + "\n" +
                "Temperatur: " + temperature + " °C\n" +
                "Luftfeuchtigkeit: " + humidity + "%\n" +
                "UV-Index: " + uvIndex + "\n" +
                "Wind: " + windKph + " km/h (" + windDir + ")";
    }
    // Getter-Methoden

    public String getCondition() {
        return condition;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getUvIndex() {
        return uvIndex;
    }

    public double getWindKph() {
        return windKph;
    }

    public String getWindDir() {
        return windDir;
    }

    public String getCityName() {
        return cityName;
    }
}

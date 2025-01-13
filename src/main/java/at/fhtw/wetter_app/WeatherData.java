package at.fhtw.wetter_app;

public class WeatherData {

    private String condition;
    private double temperature;
    private int humidity;
    private double uvIndex;

    private double windKph;  // Wind speed in kph
    private String windDir; // Wind direction

    public WeatherData(String condition, double temperature, int humidity, double uvIndex, double windKph, String windDir) {
        this.condition = condition;
        this.temperature = temperature;
        this.humidity = humidity;
        this.uvIndex = uvIndex;
        this.windKph = windKph;
        this.windDir = windDir;
    }

    // Getters
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



}

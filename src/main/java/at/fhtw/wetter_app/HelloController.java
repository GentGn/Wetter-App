package at.fhtw.wetter_app;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label welcomeText;
    @FXML
    private Label conditionLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label uvIndexLabel;
    private WeatherData simulatedWeatherData;

    @FXML
    private Label windInfoLabel;
    // Counter to toggle between wind data
    private int windInfoIndex = 0;
    // Array to cycle through wind data
    private String[] windData;


    public HelloController() {
        // Simulating the API response
        simulatedWeatherData = new WeatherData("Sunny", 25.5, 60, 5.2, 150, "North");
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onShowWeatherButton() {
        // Display simulated weather data
        conditionLabel.setText("Condition: " + simulatedWeatherData.getCondition());
        temperatureLabel.setText("Temperature: " + simulatedWeatherData.getTemperature() + "°C");
        humidityLabel.setText("Humidity: " + simulatedWeatherData.getHumidity() + "%");
        uvIndexLabel.setText("UV Index: " + simulatedWeatherData.getUvIndex());
    }


    @FXML
    protected void onFetchWeatherClick() {
        // Simulate fetching weather data (future API integration)
        windInfoLabel.setText("Weather data fetched! Click 'Toggle Wind Info' to view wind details.");
    }

    @FXML
    protected void onToggleWindInfoClick() {
            // If windData hasn't been initialized, initialize it with current wind info
            if (windData == null) {
                windData = new String[]{
                        "Wind Speed: " + simulatedWeatherData.getWindKph() + " kph",
                        "Wind Direction: " + simulatedWeatherData.getWindDir(),
                };
            }



        // Display the current wind data
        windInfoLabel.setText(windData[windInfoIndex]);

        // Update index to toggle through wind data
        windInfoIndex = (windInfoIndex + 1) % windData.length;
    }

}
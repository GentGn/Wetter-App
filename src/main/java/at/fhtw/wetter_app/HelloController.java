package at.fhtw.wetter_app;

import at.fhtw.wetter_app.utilities.CityEncoder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class HelloController {

    @FXML
    private Label conditionLabel;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label humidityLabel;
    @FXML
    private Label uvIndexLabel;
    @FXML
    private Label windInfoLabel;
    private int windInfoIndex = 0;
    private String[] windData;

    @FXML
    private Label CityWithTheHighestTemperature;

    @FXML
    private Label WeatherDetailsForCity;
    @FXML
    private TextField CityNameField;

    @FXML
    protected void onSearchCityButtonClick() {
        String city = CityNameField.getText().trim();

        if (city.isEmpty()) {
            WeatherDetailsForCity.setText("Please enter a city!");
            return;
        }

        new Thread(() -> {
            try {
                String encodedCity = CityEncoder.encodeCityName(city);
                // Fetch weather details from API
                WeatherData weatherData = WeatherServiceAPI.getWeatherData(encodedCity);

                // Update UI with the weather details
                Platform.runLater(() -> {
                    conditionLabel.setText("Condition: " + weatherData.getCondition());
                    temperatureLabel.setText("Temperature: " + weatherData.getTemperature() + "°C");
                    humidityLabel.setText("Humidity: " + weatherData.getHumidity() + "%");
                    uvIndexLabel.setText("UV Index: " + weatherData.getUvIndex());
                    windInfoLabel.setText("Wind Speed: " + weatherData.getWindKph() + " kph");
                });
            } catch (IOException e) {
                Platform.runLater(() -> WeatherDetailsForCity.setText("Error fetching data: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    protected void onToggleWindInfoClick() {
        String city = CityNameField.getText().trim();

        if (city.isEmpty()) {
            WeatherDetailsForCity.setText("Please enter a city to fetch wind info.");
            return;
        }

        new Thread(() -> {
            try {
                // Fetch weather data from the API
                WeatherData weatherData = WeatherServiceAPI.getWeatherData(CityEncoder.encodeCityName(city));

                windData = new String[]{
                "Wind Speed: " + weatherData.getWindKph() + " kph",
                "Wind Direction: " + weatherData.getWindDir()
                    };

                // Update the UI with the current wind info, toggling based on the index
                Platform.runLater(() -> windInfoLabel.setText(windData[windInfoIndex]));

                // Update the windInfoIndex to toggle between the wind speed and direction
                windInfoIndex = (windInfoIndex + 1) % windData.length;
            } catch (IOException e) {
                Platform.runLater(() -> windInfoLabel.setText("Error fetching wind data: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    protected void onShowCityWithTheHighestTemperatureButtonClick() {
        List<String> cities = Arrays.asList("Tokyo", "Delhi", "Shanghai", "São Paulo", "Mexico City", "Dhaka", "Kolkata",
                "Karachi", "Istanbul", "Buenos Aires", "Chongqing", "Lagos", "Kinshasa", "Tianjin", "Jakarta", "Cairo", "London");

        new Thread(() -> {
            try {
                // Encode city names
                List<String> encodedCities = CityEncoder.encodeCityNames(cities);

                // Get the city with the highest temperature
                String hottestCity = WeatherServiceAPI.getCityWithHighestTemperature(encodedCities);

                // Fetch weather data for the hottest city
                WeatherData response = WeatherServiceAPI.getWeatherData(hottestCity);
                double temperature = response.getTemperature();

                // Update the UI with the result
                String resultText = "The city with the highest temperature is: " + hottestCity + " - Temperature: " + temperature + "°C";

                Platform.runLater(() -> CityWithTheHighestTemperature.setText(resultText));
            } catch (Exception ex) {
                Platform.runLater(() -> CityWithTheHighestTemperature.setText("Error: " + ex.getMessage()));
            }
        }).start();
    }
}

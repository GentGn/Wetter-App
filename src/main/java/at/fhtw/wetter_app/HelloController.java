package at.fhtw.wetter_app;

import at.fhtw.wetter_app.utilities.CityEncoder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Arrays;
import java.util.List;

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

    @FXML
    private Label CityWithTheHighestTemperature;


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

    @FXML
    protected void onShowCityWithTheHighestTemperatureButtonClick(){
// Liste der Städte, für die wir das Wetter abfragen möchten
        List<String> cities = Arrays.asList( "Tokio", "Delhi", "Shanghai", "São Paulo", "Mexico City", "Dhaka", "Kalkutta",
                "Karachi", "Istanbul", "Buenos Aires", "Chongqing", "Lagos", "Kinshasa", "Tianjin",
                "Jakarta", "Lagos", "Cairo", "London", "Chengdu", "Lima", "New York City", "Bangkok",
                "Shenzhen", "Hongkong", "Hyderabad", "Dongguan", "Riyadh", "Chennai", "Guangzhou",
                "Shijiazhuang", "Kuala Lumpur", "Hong Kong", "Ahmedabad", "Hangzhou", "Xi’an", "Bagdad",
                "Paris", "Moscow", "Tianjin", "Seoul", "Kiev", "San Salvador", "Jakarta", "Bangalore",
                "Cairo", "Rome", "Santiago", "Cali", "Kinshasa", "Abidjan", "Peking", "Kolkata", "Ho Chi Minh",
                "Riyadh", "Dubai", "Lagos", "Tashkent", "Giza", "Baku", "Jinan", "Shenzhen", "Teheran",
                "Kiev", "Dhaka", "Karachi", "Guangzhou", "Düsseldorf", "Vancouver", "Lahore", "Zhengzhou",
                "San Francisco", "Chengdu", "Suzhou", "Ho Chi Minh", "Paris", "Istanbul", "Bogotá", "Montreal",
                "Jakarta", "Jakarta", "Giza", "Nairobi", "Manila", "Casablanca", "Mumbai", "Minsk", "Paris",
                "Auckland", "Santiago", "Los Angeles", "Jakarta", "Shenzhen", "São Paulo", "Chicago", "Lagos",
                "Hongkong", "Bangalore", "Hong Kong", "Toronto", "Delhi");

        new Thread(() -> {
            try {

                // Städtenamen gemäß UTF-8 encoden
                List<String> encodedCities = CityEncoder.encodeCityNames(cities);

                // Stadt mit der höchsten Temperatur abfragen
                String hottestCity = WeatherServiceAPI.getCityWithHighestTemperature(encodedCities);

                // Die Wetterdaten für diese Stadt abrufen
                String response = WeatherServiceAPI.getWeatherData(hottestCity);
                double temperature = WeatherServiceAPI.parseTemperature(response);

                // Den Stadtnamen und die Temperatur im Ergebnis-Label anzeigen
                String resultText = "Die Stadt mit der höchsten Temperatur ist: " + hottestCity +
                        " - Temperatur: " + temperature + "°C";

                // UI-Aktualisierung im JavaFX Application Thread
                Platform.runLater(() -> {
                    CityWithTheHighestTemperature.setText(resultText);
                });
            } catch (Exception ex) {
                // Fehlerbehandlung und Anzeige im UI-Thread
                Platform.runLater(() -> CityWithTheHighestTemperature.setText("Fehler: " + ex.getMessage()));
            }
        }).start();  // API-Abfrage in einem neuen Thread
    }
}
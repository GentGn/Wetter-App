package at.fhtw.wetter_app;

import at.fhtw.wetter_app.utilities.CityEncoder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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

    @FXML
    private Label WeatherDetailsForCity;
    @FXML
    private TextField CityNameField;


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
                "Shijiazhuang", "Kuala Lumpur", "Hong Kong", "Ahmedabad", "Hangzhou", "Karachi", "Bagdad",
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

    @FXML
    protected void onSearchCityButtonClick(){
// Textfeld für Benutzereingabe
        String city = CityNameField.getText().trim();

        // Validierung der Benutzereingabe
        if (city.isEmpty()) {
            WeatherDetailsForCity.setText("Bitte geben Sie eine Stadt ein.");
            return;
        }

        // Wetterdaten im Hintergrund abrufen
        new Thread(() -> {
            try {

                String encodedCity = CityEncoder.encodeCityName(city);
                // Wetterdetails abrufen
                String response = WeatherServiceAPI.getWeatherData(encodedCity);
                String weatherCondition = WeatherServiceAPI.parseWeatherCondition(response);
                double temperature = WeatherServiceAPI.parseTemperature(response);
                int humidity = WeatherServiceAPI.parseHumidity(response);
                double windSpeed = WeatherServiceAPI.parseWindSpeed(response);

                // Labels aktualisieren im JavaFX UI-Thread
                Platform.runLater(() -> {
                    WeatherDetailsForCity.setText(""); // Zuerst den Text leeren
                    Text weatherDetails = new Text("Wetterbedingungen: " + weatherCondition + "\n");
                    Text temperatureText = new Text("Temperatur: " + temperature + "°C\n");
                    Text humidityText = new Text("Luftfeuchtigkeit: " + humidity + "%\n");
                    Text windSpeedText = new Text("Windgeschwindigkeit: " + windSpeed + " km/h\n");
                    Text resultText = new Text("Wetterdaten für " + city + " erfolgreich abgerufen.");

                    TextFlow textFlow = new TextFlow(weatherDetails, temperatureText, humidityText, windSpeedText, resultText);

                    // Set the TextFlow into a Label or directly into a container
                    WeatherDetailsForCity.setGraphic(textFlow);
                });
            } catch (IOException e) {
                // Fehler im UI-Thread anzeigen
                Platform.runLater(() -> WeatherDetailsForCity.setText("Fehler beim Abrufen der Daten: " + e.getMessage()));
            }
        }).start();
    }
}
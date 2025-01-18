package at.fhtw.wetter_app;

import at.fhtw.wetter_app.WeatherDataIO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Der Controller für die Wetter-App.
 * Hier wird die Logik für die Benutzerinteraktion, das Abrufen der Wetterdaten
 * und die Aktualisierung der Benutzeroberfläche implementiert.
 */
public class HelloController {

    @FXML
    private Label conditionLabel; // Anzeige des Wetterzustands
    @FXML
    private Label temperatureLabel; // Anzeige der Temperatur
    @FXML
    private Label humidityLabel; // Anzeige der Luftfeuchtigkeit
    @FXML
    private Label uvIndexLabel; // Anzeige des UV-Index
    @FXML
    private Label windInfoLabel; // Anzeige der Winddaten
    @FXML
    private Label CityWithTheHighestTemperature; // Anzeige der heißesten Stadt
    @FXML
    private Label WeatherDetailsForCity; // Allgemeine Wetterdetails für die eingegebene Stadt
    @FXML
    private TextField CityNameField; // Eingabefeld für den Stadtnamen

    private int windInfoIndex = 0; // Index, um zwischen Windgeschwindigkeit und Richtung zu wechseln
    private String[] windData; // Array zur Speicherung von Windgeschwindigkeit und -richtung

    private boolean isCelsius = true; // Standardmäßig Celsius
    private double originalTemperature = 0.0; // Speichert die ursprüngliche Temperatur der Stadt

    private String hottestCityName = ""; // Name der heißesten Stadt
    private double hottestCityTemperatureCelsius = 0.0; // Temperatur der heißesten Stadt in Celsius

    /**
     * Initialisiert den Controller und lädt gespeicherte Wetterdaten (falls vorhanden).
     * Diese werden in die Benutzeroberfläche geladen.
     */
    @FXML
    public void initialize() {
        WeatherData savedData = WeatherDataIO.loadWeatherData();
        if (savedData != null) {
            conditionLabel.setText("Wetterzustand: " + savedData.getCondition());
            temperatureLabel.setText("Temperatur: " + savedData.getTemperature() + "°C");
            humidityLabel.setText("Luftfeuchtigkeit: " + savedData.getHumidity() + "%");
            uvIndexLabel.setText("UV-Index: " + savedData.getUvIndex());
            windInfoLabel.setText("Wind: " + savedData.getWindKph() + " km/h");
            WeatherDetailsForCity.setText("Gespeicherte Daten für: " + savedData.getCityName());
            System.out.println("Gespeicherte Wetterdaten geladen.");
        } else {
            System.out.println("Keine gespeicherten Wetterdaten gefunden.");
        }
    }

    /**
     * Wird aufgerufen, wenn der Benutzer auf "Suche Stadt" klickt.
     * Ruft die Wetterdaten für die eingegebene Stadt ab und aktualisiert die Benutzeroberfläche.
     */
    @FXML
    protected void onSearchCityButtonClick() {
        String city = CityNameField.getText().trim();

        if (city.isEmpty()) {
            WeatherDetailsForCity.setText("Bitte geben Sie eine Stadt ein!");
            return;
        }

        System.out.println("Abrufen der Wetterdaten für: " + city);

        new Thread(() -> {
            try {
                WeatherData weatherData = WeatherServiceAPI.getWeatherData(city);

                Platform.runLater(() -> {
                    conditionLabel.setText("Wetterzustand: " + weatherData.getCondition());
                    originalTemperature = weatherData.getTemperature();
                    updateTemperatureDisplay(); // Temperatur in der aktuellen Einheit anzeigen
                    humidityLabel.setText("Luftfeuchtigkeit: " + weatherData.getHumidity() + "%");
                    uvIndexLabel.setText("UV-Index: " + weatherData.getUvIndex());
                    windInfoLabel.setText("Wind: " + weatherData.getWindKph() + " km/h");
                    WeatherDetailsForCity.setText("Wetterdetails für: " + city);
                    WeatherDataIO.saveWeatherData(weatherData); // Wetterdaten speichern
                });
            } catch (IOException e) {
                Platform.runLater(() -> WeatherDetailsForCity.setText("Fehler beim Abrufen der Daten für: " + city));
                System.err.println("Fehler: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Schaltet die Temperatureinheit (Celsius <-> Fahrenheit) um und aktualisiert die Anzeige.
     */
    @FXML
    private void toggleTemperatureUnit() {
        isCelsius = !isCelsius;
        System.out.println("Temperatureinheit gewechselt: " + (isCelsius ? "Celsius" : "Fahrenheit"));
        updateTemperatureDisplay();
        updateHighestTemperatureDisplay();
    }

    /**
     * Aktualisiert die Temperaturanzeige für die aktuelle Stadt basierend auf der gewählten Einheit.
     */
    private void updateTemperatureDisplay() {
        if (isCelsius) {
            temperatureLabel.setText(String.format("Temperatur: %.1f°C", originalTemperature));
        } else {
            double temperatureInF = originalTemperature * 9 / 5 + 32;
            temperatureLabel.setText(String.format("Temperatur: %.1f°F", temperatureInF));
        }
    }

    /**
     * Schaltet zwischen der Anzeige von Windgeschwindigkeit und Windrichtung um.
     */
    @FXML
    protected void onToggleWindInfoClick() {
        String city = CityNameField.getText().trim();

        if (city.isEmpty()) {
            WeatherDetailsForCity.setText("Bitte geben Sie eine Stadt ein!");
            return;
        }

        System.out.println("Wechsel der Windinformationen für: " + city);

        new Thread(() -> {
            try {
                WeatherData weatherData = WeatherServiceAPI.getWeatherData(city);
                windData = new String[]{
                        "Windgeschwindigkeit: " + weatherData.getWindKph() + " km/h",
                        "Windrichtung: " + weatherData.getWindDir()
                };

                Platform.runLater(() -> windInfoLabel.setText(windData[windInfoIndex]));
                windInfoIndex = (windInfoIndex + 1) % windData.length;
            } catch (IOException e) {
                Platform.runLater(() -> windInfoLabel.setText("Fehler beim Abrufen der Winddaten."));
                System.err.println("Fehler: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Ruft die heißeste Stadt aus einer vorgegebenen Liste ab und zeigt die Daten an.
     */
    @FXML
    protected void onShowCityWithTheHighestTemperatureButtonClick() {
        List<String> cities = Arrays.asList(
                "Tokyo", "Delhi", "Shanghai", "São Paulo", "Mexico City",
                "Dhaka", "Kolkata", "Karachi", "Istanbul", "Buenos Aires",
                "Chongqing", "Lagos", "Kinshasa", "Tianjin", "Jakarta",
                "Kairo", "London"
        );

        System.out.println("Suche nach der heißesten Stadt...");

        new Thread(() -> {
            try {
                String hottestCity = WeatherServiceAPI.getCityWithHighestTemperature(cities);
                WeatherData response = WeatherServiceAPI.getWeatherData(hottestCity);

                hottestCityName = hottestCity;
                hottestCityTemperatureCelsius = response.getTemperature();

                Platform.runLater(this::updateHighestTemperatureDisplay);
            } catch (Exception ex) {
                Platform.runLater(() -> CityWithTheHighestTemperature.setText("Fehler bei der Ermittlung der heißesten Stadt."));
                System.err.println("Fehler: " + ex.getMessage());
            }
        }).start();
    }

    /**
     * Aktualisiert die Anzeige der heißesten Stadt und ihrer Temperatur basierend auf der aktuellen Temperatureinheit.
     */
    private void updateHighestTemperatureDisplay() {
        if (hottestCityName.isEmpty()) {
            CityWithTheHighestTemperature.setText("");
            return;
        }

        double displayedTemp = hottestCityTemperatureCelsius;
        String unitSymbol = "°C";

        if (!isCelsius) {
            displayedTemp = hottestCityTemperatureCelsius * 9 / 5 + 32;
            unitSymbol = "°F";
        }

        String resultText = "Heißeste Stadt: " + hottestCityName
                + " - Temperatur: " + String.format("%.1f", displayedTemp) + unitSymbol;

        CityWithTheHighestTemperature.setText(resultText);
    }
}

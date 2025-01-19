package at.fhtw.wetter_app;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP-Server für Wetterdatenabfragen.
 */
public class WeatherServer {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("WeatherServer läuft auf Port " + PORT);

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Verbindung von: " + clientSocket.getInetAddress());

                    // Stadtname vom Client lesen und behandeln
                    String city;
                    while ((city = in.readLine()) != null) {
                        if (city.equalsIgnoreCase("quit")) {
                            System.out.println("Verbindung beendet von: " + clientSocket.getInetAddress());
                            break;  // Beendet die Anfrage, wenn der Client "quit" sendet
                        }

                        System.out.println("Anfrage für Stadt: " + city);

                        // Wetterdaten abrufen
                        try {
                            WeatherData weatherData = WeatherServiceAPI.getWeatherData(city);
                            out.println(weatherData.toString()); // Antwort an den Client senden
                            System.out.println("Anfrage für Stadt " + city + " bearbeitet");
                        } catch (IOException e) {
                            out.println("Fehler beim Abrufen der Wetterdaten: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Fehler bei der Verarbeitung der Verbindung: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Fehler beim Starten des Servers: " + e.getMessage());
        }
    }
}

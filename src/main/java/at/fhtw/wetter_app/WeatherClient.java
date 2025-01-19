package at.fhtw.wetter_app;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP-Client für Wetterdatenabfragen.
 */
public class WeatherClient {

    private static final String SERVER_ADDRESS = "localhost"; // Server-Adresse
    private static final int SERVER_PORT = 12345;            // Server-Port

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Wetter Client gestartet. Geben Sie die Stadt ein, für die Sie Wetterdaten erhalten möchten.");
            System.out.println("Tippen Sie 'quit', um das Programm zu beenden.");

            String city;
            while (true) {
                // Benutzer zur Eingabe der Stadt auffordern
                System.out.print("Stadtname: ");
                city = scanner.nextLine();

                if (city.equalsIgnoreCase("quit")) {
                    System.out.println("Beende das Programm.");
                    break;  // Beendet die Schleife, wenn der Benutzer "quit" eingibt
                }

                // Stadtname senden
                out.println(city);

                // Antwort vom Server empfangen und anzeigen
                System.out.println("Antwort vom Server: ");
                for (int i = 0; i < 6; i++) {  // Erwarte genau 6 Zeilen
                    String response = in.readLine();
                    if (response != null) {
                        System.out.println(response);  // Gibt jede Zeile der Antwort des Servers aus
                    }
                }

                // Nachdem eine Antwort erhalten wurde, warten wir auf die nächste Benutzereingabe
            }

        } catch (IOException e) {
            System.err.println("Fehler beim Verbinden mit dem Server: " + e.getMessage());
        }
    }
}

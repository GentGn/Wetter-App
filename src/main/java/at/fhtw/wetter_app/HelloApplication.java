package at.fhtw.wetter_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Die Hauptanwendungsklasse für die Wetter-App.
 * Startet die JavaFX-Anwendung und lädt das FXML-Layout.
 */
public class HelloApplication extends Application {

    /**
     * Initialisiert und zeigt das Hauptfenster der Anwendung.
     *
     * @param stage Das Hauptfenster (Primary Stage).
     * @throws IOException Falls das FXML nicht geladen werden kann.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("7133364.png")));
        stage.setTitle("Weather App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Der Einstiegspunkt der Anwendung.
     *
     * @param args Programmargumente (nicht genutzt).
     */
    public static void main(String[] args) {
        launch();
    }
}

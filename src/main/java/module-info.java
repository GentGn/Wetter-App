module at.fhtw.wetter_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.core; // Für JSON-Verarbeitung
    requires com.fasterxml.jackson.databind; // Für ObjectMapper und JSON-Bindung

    opens at.fhtw.wetter_app to javafx.fxml, com.fasterxml.jackson.databind;

    // Exportiert die Pakete
    exports at.fhtw.wetter_app;
}

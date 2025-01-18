module at.fhtw.wetter_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.core; // For JsonProcessingException
    requires com.fasterxml.jackson.databind; // For ObjectMapper and JSON binding

    opens at.fhtw.wetter_app to javafx.fxml;
    exports at.fhtw.wetter_app;
}
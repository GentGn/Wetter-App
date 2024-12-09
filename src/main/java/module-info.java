module at.fhtw.wetter_app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens at.fhtw.wetter_app to javafx.fxml;
    exports at.fhtw.wetter_app;
}
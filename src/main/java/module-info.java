module demo.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens demo.hotel to javafx.fxml;
    opens model to javafx.fxml;
    opens controller to javafx.fxml;

    exports demo.hotel;
    exports controller;
    exports model;
}

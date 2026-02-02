module com.example.calcolatrice {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.calcolatrice to javafx.fxml;
    exports com.example.calcolatrice;
    exports com.example.calcolatrice.controller;
    opens com.example.calcolatrice.controller to javafx.fxml;
}
package com.example.calcolatrice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view/main-view.fxml"));
        // Dimensioni aumentate per mostrare correttamente il contenuto
        Scene scene = new Scene(fxmlLoader.load(), 420, 760);
        stage.setTitle("calcolatrice");
        stage.setScene(scene);

        // Impostato larghezza e altezza minima
        stage.setMinWidth(420);
        stage.setMinHeight(760);
        stage.centerOnScreen();

        stage.show();
    }

    public static void main(String[] args){
        launch();
    }
}
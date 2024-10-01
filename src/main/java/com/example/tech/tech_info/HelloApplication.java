package com.example.tech.tech_info;

import com.example.tech.tech_info.dao.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.connect();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/loginPage/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Customer Management");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading FXML: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

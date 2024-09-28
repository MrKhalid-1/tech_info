package com.example.tech.tech_info;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/customer/Customer.fxml"));
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
/*

package org.example.tech_info1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/tech_info1/fxml/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setTitle("Customer Management System");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print the stack trace to console
        }
    }

}
 */
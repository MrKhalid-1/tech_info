package com.example.tech.tech_info.controller.loginPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.tech.tech_info.SceneSwitcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateUser(username, password)) {
            System.out.println("Login successful");
            // Redirect to the customer management screen
            Stage stage = (Stage) usernameField.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "/com/example/tech/tech_info/fxml/customer/Customer.fxml");
        } else {
            System.out.println("Invalid username or password");
            showErrorLogin("Invalid credentials!"); // Show error message
        }
    }

    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/loginPage/Signup.fxml"));
//            C:\Users\abkha\OneDrive\Desktop\tech_info\src\main\resources\com\example\tech\tech_info\fxml\customer\Customer.fxml
            Parent signupView = loader.load();
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            Scene scene = new Scene(signupView);
            currentStage.setScene(scene);
            currentStage.setTitle("Signup");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error loading signup view");
        }
    }


    private boolean validateUser(String username, String password) {
        boolean isValid = false;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:sqliteTest/management.db")) {

            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            isValid = rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }


    private void showErrorLogin(String message) {
        // Display error message to the user, e.g., using an alert
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility methods to show alerts
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }

}

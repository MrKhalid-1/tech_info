package com.example.tech.tech_info.controller.loginPage;

import com.example.tech.tech_info.SceneSwitcher;
import com.example.tech.tech_info.dao.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
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
            Stage stage = (Stage) usernameField.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "/com/example/tech/tech_info/fxml/customer/Customer.fxml");
        } else {
            System.out.println("Invalid username or password");
            showErrorLogin("Invalid credentials!");
        }
    }

    @FXML
    private void handleSignup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/loginPage/Signup.fxml"));
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
        try (Connection conn = DatabaseConnection.getConnection()) {
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}

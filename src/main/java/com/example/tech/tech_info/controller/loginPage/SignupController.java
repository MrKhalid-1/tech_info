package com.example.tech.tech_info.controller.loginPage;

import com.example.tech.tech_info.SceneSwitcher;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

public class SignupController {

    @FXML
    private TextField signupUsernameField;
    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private void handleSignup() {
        String username = signupUsernameField.getText();
        String password = signupPasswordField.getText();

        // Check if user already exists
        if (!isUserRegistered()) {
            saveUser(username, password);
            System.out.println("User created successfully!");
            switchToLoginScreen(); // Switch to login screen after success
        } else {
            System.out.println("User already exists! Only one user is allowed.");
            showError("User already exists! Only one user is allowed.");
            switchToLoginScreen(); // Switch to login screen after success
        }
    }

    private boolean isUserRegistered() {
        boolean exists = false;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:sqliteTest/management.db")) {
            String sql = "SELECT COUNT(*) FROM user";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            exists = rs.getInt(1) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    private void saveUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:sqliteTest/management.db")) {
            String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchToLoginScreen() {
        Stage stage = (Stage) signupUsernameField.getScene().getWindow(); // Get current window (stage)
        SceneSwitcher.switchTo(stage, "/com/example/tech/tech_info/fxml/loginPage/Login.fxml") ;// Switch to login.fxml
    }
}

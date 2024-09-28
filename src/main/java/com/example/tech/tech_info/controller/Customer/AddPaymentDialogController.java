package com.example.tech.tech_info.controller.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static com.example.tech.tech_info.dao.DatabaseConnection.getConnection;

public class AddPaymentDialogController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField commentField;


    private int customerId; // Set this from the parent controller

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void updateCustomerPayment(int customerId, double paymentAmount) {
        String sql = "UPDATE customers SET payment = payment + ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // Disable auto-commit
            pstmt.setDouble(1, paymentAmount);
            pstmt.setInt(2, customerId);
            int rowsUpdated = pstmt.executeUpdate();
            conn.commit();
            if (rowsUpdated > 0) {
                System.out.println("Payment updated successfully.");
            } else {
                System.out.println("No customer found with ID: " + customerId);
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Unable to update payment. Please try again.");
            e.printStackTrace(); // Log the exception
        }
    }

    public void addPaymentToHistory(int customerId, String comment, double paymentAmount) {
        String sql = "INSERT INTO historyPayment (customerId, comment, amount) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, comment);
            pstmt.setDouble(3, paymentAmount);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            showAlert("Database Error", "Unable to record payment history. Please try again.");
            e.printStackTrace(); // Log the exception
        }
    }

    @FXML
    private void handleAddPayment() {
        String amountText = amountField.getText();
        String commentText = commentField.getText();

        if (amountText.isEmpty() || commentText.isEmpty()) {
            showAlert("Input Error", "Please fill in both amount and comment fields.");
            return;
        }

        try {
            double paymentAmount = Double.parseDouble(amountText);

            if (paymentAmount <= 0) {
                showAlert("Invalid Amount", "Please enter a positive amount.");
                return;
            }

            // Confirmation alert
            if (showConfirmationAlert("Add Payment", "Are you sure you want to add " + paymentAmount + " as a payment?")) {
                updateCustomerPayment(customerId, paymentAmount);
                addPaymentToHistory(customerId, commentText, paymentAmount);
                showAlert("Payment Added", "Payment of " + paymentAmount + " added successfully.");
                refreshCustomerData();
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Amount", "Please enter a valid numeric amount.");
        }
    }

    private boolean showConfirmationAlert(String title, String message) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(message);
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }


//    @FXML
//    private void handleAddPayment() {
//        String amountText = amountField.getText();
//        String commentText = commentField.getText();
//
//        if (amountText.isEmpty() || commentText.isEmpty()) {
//            showAlert("Input Error", "Please fill in both amount and comment fields.");
//            return;
//        }
//
//        try {
//            double paymentAmount = Double.parseDouble(amountText);
//
//             Check for positive payment amount
//            if (paymentAmount <= 0) {
//                showAlert("Invalid Amount", "Please enter a positive amount.");
//                return;
//            }
//
//             Confirmation alert
//            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
//            confirmationAlert.setTitle("Add Payment");
//            confirmationAlert.setHeaderText(null);
//            confirmationAlert.setContentText("Are you sure you want to add " + paymentAmount + " as a payment?");
//
//            if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
//                // Update the customer's payment amount
//                updateCustomerPayment(customerId, paymentAmount);
//
//                // Add the payment record to history
//                addPaymentToHistory(customerId, commentText, paymentAmount);
//
//                // Show a success message
//                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
//                successAlert.setTitle("Payment Added");
//                successAlert.setHeaderText(null);
//                successAlert.setContentText("Payment of " + paymentAmount + " added successfully.");
//                successAlert.showAndWait();
//
//                // Refresh the customer data in the UI
//                refreshCustomerData();
//            }
//        } catch (NumberFormatException e) {
//            showAlert("Invalid Amount", "Please enter a valid numeric amount.");
//        }
//    }

    private void refreshCustomerData() {
        String sql = "SELECT payment FROM customers WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double updatedPayment = rs.getDouble("payment");
                // Assuming you have a label or another UI component to show the payment
                amountField.setText(String.valueOf(updatedPayment)); // Update the payment display
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Unable to refresh customer data. Please try again.");
            e.printStackTrace(); // Log the exception
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

//package com.example.tech.tech_info.controller.Customer;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.ButtonType;
//import javafx.scene.control.TextField;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import static com.example.tech.tech_info.dao.DatabaseConnection.getConnection;
//
//public class AddPaymentDialogController {
//
//    @FXML
//    private TextField amountField;
//
//    @FXML
//    private TextField commentField;
//
//    private int customerId; // Set this from the parent controller
//
//    public void setCustomerId(int customerId) {
//        this.customerId = customerId;
//    }
//
//    public void updateCustomerPayment(int customerId, double paymentAmount) {
//        String sql = "UPDATE customers SET payment = payment + ? WHERE id = ?";
//        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            conn.setAutoCommit(false); // Disable auto-commit
//            pstmt.setDouble(1, paymentAmount);
//            pstmt.setInt(2, customerId);
//            int rowsUpdated = pstmt.executeUpdate();
//            conn.commit();
//            if (rowsUpdated > 0) {
//                System.out.println("Payment updated successfully.");
//            } else {
//                System.out.println("No customer found with ID: " + customerId);
//            }
//        } catch (SQLException e) {
//            showAlert("Database Error", "Unable to update payment. Please try again.");
//            e.printStackTrace(); // Log the exception
//        }
//    }
//
//
//    public void addPaymentToHistory(int customerId, String comment, double paymentAmount) {
//        String sql = "INSERT INTO historyPayment (customerId, comment, amount) VALUES (?, ?, ?)";
//        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, customerId);
//            pstmt.setString(2, comment);
//            pstmt.setDouble(3, paymentAmount);
//            pstmt.executeUpdate();
//        } catch (SQLException e) {
//            showAlert("Database Error", "Unable to record payment history. Please try again.");
//            e.printStackTrace(); // Log the exception
//        }
//    }
//
//    @FXML
//    private void handleAddPayment() {
//        String amountText = amountField.getText();
//        String commentText = commentField.getText();
//
//        if (amountText.isEmpty() || commentText.isEmpty()) {
//            showAlert("Input Error", "Please fill in both amount and comment fields.");
//            return;
//        }
//
//        try {
//            double paymentAmount = Double.parseDouble(amountText);
//
//            // Check for positive payment amount
//            if (paymentAmount <= 0) {
//                showAlert("Invalid Amount", "Please enter a positive amount.");
//                return;
//            }
//
//            // Confirmation alert
//            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
//            confirmationAlert.setTitle("Add Payment");
//            confirmationAlert.setHeaderText(null);
//            confirmationAlert.setContentText("Are you sure you want to add " + paymentAmount + " as a payment?");
//
//            if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
//                // Update the customer's payment amount
//                updateCustomerPayment(customerId, paymentAmount);
//
//                // Add the payment record to history
//                addPaymentToHistory(customerId, commentText, paymentAmount);
//
//                // Show a success message
//                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
//                successAlert.setTitle("Payment Added");
//                successAlert.setHeaderText(null);
//                successAlert.setContentText("Payment of " + paymentAmount + " added successfully.");
//                successAlert.showAndWait();
//                refreshCustomerData();
//            }
//        } catch (NumberFormatException e) {
//            showAlert("Invalid Amount", "Please enter a valid numeric amount.");
//        }
//    }
//
//
//    private void showAlert(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//}

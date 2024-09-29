package com.example.tech.tech_info.controller.Customer;

import com.example.tech.tech_info.dao.DatabaseConnection;
import com.example.tech.tech_info.entity.TCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;

import static com.example.tech.tech_info.dao.DatabaseConnection.getConnection;

public class AddPaymentDialogController {

    @FXML
    private TextField amountField;

    @FXML
    private TextField commentField;

    @FXML
    private TableView<TCustomer.Customer> customerTable;

    private ObservableList<TCustomer.Customer> customerData = FXCollections.observableArrayList();
    private int customerId; // Set this from the parent controller

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void updateCustomerPayment(int customerId, double paymentAmount) {
        String sql = "UPDATE customers SET payment = payment + ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            pstmt.setDouble(1, paymentAmount);
            pstmt.setInt(2, customerId);
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated);
            System.out.println(customerId+"p  "+paymentAmount);
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

            if (showConfirmationAlert("Add Payment", "Are you sure you want to add " + paymentAmount + " as a payment?")) {
                updateCustomerPayment(customerId, paymentAmount);
                addPaymentToHistory(customerId, commentText, paymentAmount);
//                showAlert("Payment Added", "Payment of " + paymentAmount + " added successfully.");
                Stage stage = (Stage) amountField.getScene().getWindow();
                stage.close();

                 // Refresh the data after deletion
                // Notify or trigger the parent controller (Customer.fxml) to update payment information
//                returnToCustomerView();
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


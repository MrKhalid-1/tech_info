package com.example.tech.tech_info.controller.Customer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCustomerDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField mobileField;

    @FXML
    private TextField aadharCardNumberField;

    private CustomerController mainController;

    public void setMainController(CustomerController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAddCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String mobileText = mobileField.getText();
        String aadharCardNumber = aadharCardNumberField.getText();
        if (name == null || name.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                mobileText == null || mobileText.trim().isEmpty()) {
            showAlert("Input Error", "Required fields must be filled out.");
            return;
        }
        try {
            Double payment = 0.0 ;
            mainController.addCustomerToDatabase(name, address, mobileText, aadharCardNumber, payment);
            mainController.loadCustomerData();
            closeDialog();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Payment");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid payment amount.");
            alert.showAndWait();
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

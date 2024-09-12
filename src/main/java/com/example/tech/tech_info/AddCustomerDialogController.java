package com.example.tech.tech_info;

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
    private TextField paymentField;

    private CustomerController mainController;

    public void setMainController(CustomerController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleAddCustomer() {
        String name = nameField.getText();
        String address = addressField.getText();
        String mobileText = mobileField.getText();
        String paymentText = paymentField.getText();

        if (name == null || name.trim().isEmpty() ||
                address == null || address.trim().isEmpty() ||
                mobileText == null || mobileText.trim().isEmpty() ||
                paymentText == null || paymentText.trim().isEmpty()) {
            showAlert("Input Error", "All fields must be filled out.");
            return;
        }
        try {
            Double payment = Double.parseDouble(paymentText);
            // Passing mobileText as String
            mainController.addCustomerToDatabase(name, address, mobileText, payment);
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

//    @FXML
//    private void handleAddCustomer() {
//        String name = nameField.getText();
//        String address = addressField.getText();
//        String mobileText = mobileField.getText();
//        String paymentText = paymentField.getText();
//
//        if (name == null || name.trim().isEmpty() ||
//                address == null || address.trim().isEmpty() ||
//                mobileText == null || mobileText.trim().isEmpty() ||
//                paymentText == null || paymentText.trim().isEmpty()) {
//            showAlert("Input Error", "All fields must be filled out.");
//            return;
//        }
//        try {
//            Integer mobile = Integer.parseInt(mobileText);
//            Double payment = Double.parseDouble(paymentText);
//            mainController.addCustomerToDatabase(name, address, mobile ,payment);
//            mainController.loadCustomerData();
//            closeDialog();
//        } catch (NumberFormatException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid Payment");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter a valid payment amount.");
//            alert.showAndWait();
//        }
//    }
//
//    private void closeDialog() {
//        Stage stage = (Stage) nameField.getScene().getWindow();
//        stage.close();
//    }
//
//    private void showAlert(String title, String content) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        alert.showAndWait();
//    }
//}
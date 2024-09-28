package com.example.tech.tech_info.controller.Customer;

import com.example.tech.tech_info.entity.TCustomer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UpdateCustomerDialogController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField mobileField;

    @FXML
    private TextField aadharCardNumberField;

    @FXML
    private TextField paymentField;

    @FXML
    private Button updateButton;

    @FXML
    private Button cancelButton;

    private CustomerController mainController;
    private TCustomer.Customer customer;

    @FXML
    private void initialize() {
        updateButton.setOnAction(event -> handleUpdate());
        cancelButton.setOnAction(event -> ((Stage) cancelButton.getScene().getWindow()).close());
    }

    public void setMainController(CustomerController mainController) {
        this.mainController = mainController;
    }

    public void setCustomer(TCustomer.Customer customer) {
        this.customer = customer;
        nameField.setText(customer.getName());
        addressField.setText(customer.getAddress());
        mobileField.setText(String.valueOf(customer.getMobile()));
        aadharCardNumberField.setText(customer.getAadharCardNumber());
        paymentField.setText(String.valueOf(customer.getPayment()));
    }

        private void handleUpdate () {
            String name = nameField.getText();
            String address = addressField.getText();
            Integer mobile = null;
            String aadharCardNumber = aadharCardNumberField.getText();
            Double payment = null;

            try {
                mobile = Integer.parseInt(mobileField.getText());
                payment = Double.parseDouble(paymentField.getText());
            } catch (NumberFormatException e) {
                mainController.showAlert("Invalid Input", "Please enter valid numbers for mobile and payment.");
                return;
            }

            if (name.isEmpty() || address.isEmpty() || mobileField.getText().isEmpty()) {
                mainController.showAlert("Missing Information", "Please fill in all fields.");
                return;
            }

            // Call the method to update the customer in the database
            mainController.updateCustomerInDatabase(customer.getId(), name, address, mobile,aadharCardNumber,payment);

            // Close the dialog
            ((Stage) updateButton.getScene().getWindow()).close();
        }
    }

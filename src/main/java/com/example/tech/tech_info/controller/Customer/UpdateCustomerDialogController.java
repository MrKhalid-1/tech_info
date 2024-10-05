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
        mobileField.setText(customer.getMobile());
        aadharCardNumberField.setText(customer.getAadharCardNumber());
    }

        private void handleUpdate () {
            String name = nameField.getText();
            String address = addressField.getText();
            String mobile = mobileField.getText();
            String aadharCardNumber = aadharCardNumberField.getText();
            Double payment = null;

            if (name.isEmpty() || address.isEmpty() || mobile.isEmpty()) {
                mainController.showAlert("Missing Information", "Please fill in all fields.");
                return;
            }

            mainController.updateCustomerInDatabase(customer.getId(), name, address,mobile,aadharCardNumber,customer.getPayment());
            ((Stage) updateButton.getScene().getWindow()).close();
        }
    }

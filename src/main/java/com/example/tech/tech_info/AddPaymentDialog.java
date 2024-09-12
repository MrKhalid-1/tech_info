package com.example.tech.tech_info;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AddPaymentDialog extends Dialog<Double> {

    private final TextField paymentField = new TextField();

    public AddPaymentDialog(Customer customer) {
        setTitle("Add Payment");
        setHeaderText("Add payment for customer: " + customer.getName());

        DialogPane dialogPane = new DialogPane();
        dialogPane.setStyle("-fx-padding: 20;");

        // Set up the grid pane for input fields
        GridPane grid = new GridPane();
        grid.add(new Label("Payment Amount:"), 0, 0);
        grid.add(paymentField, 1, 0);

        dialogPane.setContent(grid);

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        getDialogPane().getButtonTypes().addAll(addButtonType, cancelButtonType);

        // Convert result to payment amount
        setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    return Double.parseDouble(paymentField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });
    }
}


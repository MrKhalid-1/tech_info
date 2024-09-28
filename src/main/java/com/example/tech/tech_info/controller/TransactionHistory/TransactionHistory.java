package com.example.tech.tech_info.controller.TransactionHistory;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class TransactionHistory {

    private int customerId;

    @FXML
    private ListView<String> transactionListView; // Assuming you have a ListView to show transactions

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void loadTransactionHistory() {
        // Logic to load transaction history based on customerId
        // For demonstration, we're using a dummy list of transactions
        transactionListView.getItems().clear(); // Clear existing items

        // Example transactions based on customerId (replace with actual data retrieval)
        if (customerId != 0) {
            transactionListView.getItems().addAll(
                    "Transaction 1 for customer " + customerId,
                    "Transaction 2 for customer " + customerId,
                    "Transaction 3 for customer " + customerId
            );
        } else {
            transactionListView.getItems().add("No transactions found.");
        }
    }
}

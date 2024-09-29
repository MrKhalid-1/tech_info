package com.example.tech.tech_info.controller.TransactionHistory;

import com.example.tech.tech_info.entity.TPaymentHistory;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TransactionHistory {

    private int customerId;

    @FXML
    private TableView<TPaymentHistory> historyTable;
    @FXML
    private TableColumn<TPaymentHistory, Long> idColumn;
    @FXML
    private TableColumn<TPaymentHistory, Long> customerIdColumn;
    @FXML
    private TableColumn<TPaymentHistory, String> commentColumn;
    @FXML
    private TableColumn<TPaymentHistory, Double> amountColumn;
    @FXML
    private TableColumn<TPaymentHistory, LocalDate> receivedDateColumn;

    @FXML
    public void initialize() {
        // Setting up cell value factories for the table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        receivedDateColumn.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));

        // Customizing the row factory for hover effects
        historyTable.setRowFactory(tv -> new TableRow<TPaymentHistory>() {
            @Override
            protected void updateItem(TPaymentHistory item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle(""); // Clear style if the row is empty
                } else {
                    setOnMouseEntered(event -> setStyle("-fx-background-color: #e9ecef;")); // Hover effect
                    setOnMouseExited(event -> setStyle("")); // Remove hover effect
                }
            }
        });
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void loadTransactionHistory() {
        historyTable.getItems().clear(); // Clear existing items
        String query = "SELECT * FROM historyPayment WHERE customerId = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:sqliteTest/management.db");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDate receivedDate = null;
                String receivedDateString = rs.getString("received_date"); // Read as String
                if (receivedDateString != null && !receivedDateString.isEmpty()) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        receivedDate = LocalDate.parse(receivedDateString, formatter); // Manually parse to LocalDate
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                }

                TPaymentHistory historyPayment = new TPaymentHistory(
                        rs.getLong("id"),
                        rs.getLong("customerId"),
                        rs.getString("comment"),
                        rs.getDouble("amount"),
                        receivedDate
                );

                historyTable.getItems().add(historyPayment);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

package com.example.tech.tech_info.controller.TransactionHistory;

import com.example.tech.tech_info.dao.DatabaseConnection;
import com.example.tech.tech_info.entity.TPaymentHistory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

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
    private TableColumn<TPaymentHistory, String> imageColumn;

    @FXML
    private TableColumn<TPaymentHistory, Void> deleteColumn;

    @FXML
    private TableColumn<TPaymentHistory, Void> updateColumn;

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        receivedDateColumn.setCellValueFactory(new PropertyValueFactory<>("receivedDate"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageName"));

        // Button for Download Image
        imageColumn.setCellFactory(param -> new TableCell<>() {
            private final Button downloadButton = new Button("Download");
            @Override
            protected void updateItem(String imageName, boolean empty) {
                super.updateItem(imageName, empty);
                if (empty || imageName == null) {
                    setGraphic(null);
                } else {
                    setGraphic(downloadButton);
                    downloadButton.setOnAction(event -> {
                        TPaymentHistory history = getTableView().getItems().get(getIndex());
                        showDownloadConfirmation(history);
                    });
                }
            }
        });

        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {
                        TPaymentHistory history = getTableView().getItems().get(getIndex());
                        deleteTransaction(history);
                    });
                }
            }
        });

        updateColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateButton = new Button("Update");
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(updateButton);
                    updateButton.setOnAction(event -> {
                        TPaymentHistory history = getTableView().getItems().get(getIndex());
                        updateTransaction(history);
                    });
                }
            }
        });

        loadTransactionHistory();
    }

    public void loadTransactionHistory() {
        historyTable.getItems().clear();
        String query = "SELECT * FROM historyPayment WHERE customerId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate receivedDate = null;
                long receivedDateMillis = rs.getLong("received_date");
                if (receivedDateMillis > 0) {
                    Instant instant = Instant.ofEpochMilli(receivedDateMillis);
                    receivedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                }
                TPaymentHistory historyPayment = new TPaymentHistory(
                        rs.getLong("id"),
                        rs.getLong("customerId"),
                        rs.getString("comment"),
                        rs.getDouble("amount"),
                        receivedDate,
                        rs.getString("imageName"),
                        rs.getString("imagePath")
                );

                historyTable.getItems().add(historyPayment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTransaction(TPaymentHistory history) {
        String deleteQuery = "DELETE FROM historyPayment WHERE id = ?";
        String updateAmountQuery = "UPDATE customers SET payment = payment + ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateAmountQuery)) {
            deleteStmt.setLong(1, history.getId());
            deleteStmt.executeUpdate();
            double adjustmentAmount = history.getAmount();
            if (adjustmentAmount > 0) {
                adjustmentAmount = -adjustmentAmount;
            } else {
                adjustmentAmount = Math.abs(adjustmentAmount);
            }
            updateStmt.setDouble(1, adjustmentAmount);
            updateStmt.setLong(2, history.getCustomerId());
            updateStmt.executeUpdate();
            showAlert("Delete Successful", "Transaction deleted and payment updated successfully.");
            loadTransactionHistory();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to delete transaction.");
        }
    }


    private void updateTransaction(TPaymentHistory history) {
//      TODO it's pending
        loadTransactionHistory();
    }

    private void downloadImage(TPaymentHistory history) {
        String query = "SELECT image FROM historyPayment WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, history.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] imageBytes = rs.getBytes("image");
                if (imageBytes != null) {
                    saveImageToFile(imageBytes, history.getImageName());
                } else {
                    showAlert("No Image", "No image available for this transaction.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Unable to download image. Please try again.");
        }
    }

    private void saveImageToFile(byte[] imageBytes, String imageName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.setInitialFileName(imageName);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Files.write(file.toPath(), imageBytes);
                showAlert("Download Successful", "Image downloaded successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Download Error", "Error saving image. Please try again.");
            }
        }
    }

    private void showDownloadConfirmation(TPaymentHistory history) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Download Image");
        alert.setHeaderText("Download Image Confirmation");
        alert.setContentText("Do you want to download the image for this payment?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                downloadImage(history);
            }
        });
    }

    void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void closeWindow() {
        historyTable.getScene().getWindow().hide();
    }
}

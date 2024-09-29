package com.example.tech.tech_info.controller.Customer;

import com.example.tech.tech_info.controller.TransactionHistory.TransactionHistory;
import com.example.tech.tech_info.dao.DatabaseConnection;
import com.example.tech.tech_info.entity.TCustomer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CustomerController {

    @FXML
    private TableView<TCustomer.Customer> tableView;

    @FXML
    private TableColumn<TCustomer.Customer, Long> idColumn;

    @FXML
    private TableColumn<TCustomer.Customer, String> nameColumn;

    @FXML
    private TableColumn<TCustomer.Customer, String> addressColumn;

    @FXML
    private TableColumn<TCustomer.Customer, String> mobileColumn;

    @FXML
    private TableColumn<TCustomer.Customer, String> aadharCardNumberColumn;

    @FXML
    private TableColumn<TCustomer.Customer, Double> paymentColumn;

    @FXML
    private TextField searchField;

    @FXML
    private TextField amountField;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private Button addPaymentButton;

    @FXML
    private Button deletePaymentButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button transactionButton;

    private ObservableList<TCustomer.Customer> customerData = FXCollections.observableArrayList();
    private ObservableList<TCustomer.Customer> filteredData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        aadharCardNumberColumn.setCellValueFactory(new PropertyValueFactory<>("aadharCardNumber"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        // Load data from the database
        loadCustomerData();

        // Initially set the full customer list to the table
        tableView.setItems(customerData);

        // Add listener for search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCustomers(newValue));

        // Bind button actions
        addPaymentButton.setOnAction(event -> openAddPaymentDialog());
        deletePaymentButton.setOnAction(event -> openDeletePayment());
        addCustomerButton.setOnAction(event -> handleAddCustomer());
        deleteCustomerButton.setOnAction(event -> handleDeleteCustomer());
        updateCustomerButton.setOnAction(event -> handleUpdateCustomer());
        logoutButton.setOnAction(event -> handleLogout());
        transactionButton.setOnAction(event -> handleTransaction());
    }


    public void loadCustomerData() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            customerData.clear();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                Integer mobile = resultSet.getInt("mobile");
                String aadharCardNumber = resultSet.getString("aadharCardNumber");
                Double payment = resultSet.getDouble("payment");

                customerData.add(new TCustomer.Customer(id, name, address, mobile, aadharCardNumber, payment));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void filterCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            tableView.setItems(customerData);
        } else {
            filteredData.clear();
            for (TCustomer.Customer customer : customerData) {
                if (matchesSearch(customer, keyword)) {
                    filteredData.add(customer);
                }
            }
            tableView.setItems(filteredData);
        }
    }

    private boolean matchesSearch(TCustomer.Customer customer, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return customer.getName().toLowerCase().contains(lowerCaseKeyword)
                || customer.getAddress().toLowerCase().contains(lowerCaseKeyword)
                || customer.getPayment().toString().contains(lowerCaseKeyword);
    }

    @FXML
    private void openAddPaymentDialog() {
        TCustomer.Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("No Selection", "Please select a customer to add payment.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/customer/AddPayment.fxml"));
            Parent root = loader.load();

            // Pass the selected customer to the dialog
            AddPaymentDialogController dialogController = loader.getController();
            dialogController.setCustomerId(Math.toIntExact(selectedCustomer.getId()));
//            selectedCustomer.getId();

            Stage stage = new Stage();
            stage.setTitle("Add Payment for " + selectedCustomer.getName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadCustomerData();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open payment dialog.");
        }
    }

    @FXML
    private void openDeletePayment() {
        TCustomer.Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            showAlert("No Selection", "Please select a customer to add payment.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/customer/DeletePayment.fxml"));
            Parent root = loader.load();
            DeletePaymentDialogController dialogController = loader.getController();
            dialogController.setCustomerId(Math.toIntExact(selectedCustomer.getId()));
            Stage stage = new Stage();
            stage.setTitle("Delete Payment for " + selectedCustomer.getName());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            loadCustomerData();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open payment dialog.");
        }
    }

    @FXML
    private void handleAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/customer/AddCustomer.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableView.getScene().getWindow());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            AddCustomerDialogController controller = loader.getController();
            controller.setMainController(this);

            dialogStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteCustomer() {
        TCustomer.Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Customer");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete customer: " + selectedCustomer.getName() + "?");

            if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                deleteCustomerFromDatabase(selectedCustomer.getId());
                loadCustomerData(); // Refresh the data after deletion
            }
        } else {
            showAlert("No Selection", "Please select a customer to delete.");
        }
    }

    public void addCustomerToDatabase(String name, String address, String mobile, String aadharCardNumber, Double payment) {
        String sql = "INSERT INTO customers (name, address, mobile , aadharCardNumber,payment) VALUES (?,?,?,?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, mobile);
            preparedStatement.setString(4, aadharCardNumber);
            preparedStatement.setDouble(5, payment);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomerFromDatabase(Long customerId) {
        String sql = "DELETE FROM customers WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleUpdateCustomer() {
        TCustomer.Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/customer/UpdateCustomer.fxml"));
                Pane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Update Customer");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(tableView.getScene().getWindow());

                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                UpdateCustomerDialogController controller = loader.getController();
                controller.setMainController(this);
                controller.setCustomer(selectedCustomer);

                dialogStage.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a customer to update.");
        }
    }

    public void updateCustomerInDatabase(Long id, String name, String address, Integer mobile, String aadharCardNumber, Double payment) {
        String sql = "UPDATE customers SET name = ?, address = ?, mobile = ?,aadharCardNumber=?, payment = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setInt(3, mobile);
            preparedStatement.setString(4, aadharCardNumber);
            preparedStatement.setDouble(5, payment);
            preparedStatement.setLong(6, id);
            preparedStatement.executeUpdate();
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        // Perform logout logic, such as clearing session data
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Logout");
//        alert.setHeaderText(null);
//        alert.setContentText("You want to logged out.");

//        alert.showAndWait();

        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();  // Close the customer screen

        // Show the login screen
        showLoginScreen();
    }

    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/loginPage/Login.fxml"));
            Pane loginPage = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("Login");
            loginStage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(loginPage);
            loginStage.setScene(scene);

            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTransaction() {
        TCustomer.Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/fxml/TransactionHistory/TransactionHistory.fxml"));
                Pane page = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Transaction History");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(tableView.getScene().getWindow());

                Scene scene = new Scene(page);
                dialogStage.setScene(scene);

                // Get the controller and pass the customerId
                TransactionHistory controller = loader.getController();
                controller.setCustomerId(Math.toIntExact(selectedCustomer.getId())); // Fix here
                controller.loadTransactionHistory(); // Load transaction history based on customerId

                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("No Selection", "Please select a customer to view history.");
        }
    }


    void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean showConfirmationAlert(String title, String message) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(title);
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText(message);
        return confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    private void showAlertPayment(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
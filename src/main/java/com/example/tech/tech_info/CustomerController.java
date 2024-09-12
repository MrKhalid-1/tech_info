package com.example.tech.tech_info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class CustomerController {

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Long> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> addressColumn;

    @FXML
    private TableColumn<Customer, String> mobileColumn;

    @FXML
    private TableColumn<Customer, Double> paymentColumn;

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

    private ObservableList<Customer> customerData = FXCollections.observableArrayList();
    private ObservableList<Customer> filteredData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns in the TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        mobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        paymentColumn.setCellValueFactory(new PropertyValueFactory<>("payment"));

        // Load data from the database
        loadCustomerData();

        // Initially set the full customer list to the table
        tableView.setItems(customerData);

        // Add listener for search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterCustomers(newValue));

        // Bind button actions
        addPaymentButton.setOnAction(event -> handleAddPayment());
        deletePaymentButton.setOnAction(event -> handleDeletePayment());
        addCustomerButton.setOnAction(event -> handleAddCustomer());
        deleteCustomerButton.setOnAction(event -> handleDeleteCustomer());
        updateCustomerButton.setOnAction(event -> handleUpdateCustomer());
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
                Double payment = resultSet.getDouble("payment");

                customerData.add(new Customer(id, name, address, mobile, payment));
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
            for (Customer customer : customerData) {
                if (matchesSearch(customer, keyword)) {
                    filteredData.add(customer);
                }
            }
            tableView.setItems(filteredData);
        }
    }

    private boolean matchesSearch(Customer customer, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return customer.getName().toLowerCase().contains(lowerCaseKeyword)
                || customer.getAddress().toLowerCase().contains(lowerCaseKeyword)
                || customer.getPayment().toString().contains(lowerCaseKeyword);
    }

    @FXML
    private void handleAddPayment() {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            String amountText = amountField.getText();
            try {
                Double paymentAmount = Double.parseDouble(amountText);
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Add Payment");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to add " + paymentAmount + " to the payment for customer: " + selectedCustomer.getName() + "?");

                if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    addPaymentToDatabase(selectedCustomer.getId(), paymentAmount);
                    loadCustomerData(); // Refresh the data after adding
                    amountField.clear(); // Clear the amount field
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid amount.");
            }
        } else {
            showAlert("No Selection", "Please select a customer to add payment.");
        }
    }

    private void addPaymentToDatabase(Long customerId, Double paymentAmount) {
        String sql = "UPDATE customers SET payment = payment + ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, paymentAmount);
            preparedStatement.setLong(2, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePayment() {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            String amountText = amountField.getText();
            try {
                Double paymentAmount = Double.parseDouble(amountText);
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Delete Payment");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Are you sure you want to delete " + paymentAmount + " from the payment for customer: " + selectedCustomer.getName() + "?");

                if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    deletePaymentFromDatabase(selectedCustomer.getId(), paymentAmount);
                    loadCustomerData(); // Refresh the data after deletion
                    amountField.clear();
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid amount.");
            }
        } else {
            showAlert("No Selection", "Please select a customer to delete payment.");
        }
    }

    private void deletePaymentFromDatabase(Long customerId, Double paymentAmount) {
        String sql = "UPDATE customers SET payment = payment - ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, paymentAmount);
            preparedStatement.setLong(2, customerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/AddCustomer.fxml"));
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
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

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

    public void addCustomerToDatabase(String name, String address,String mobile, Double payment) {
        String sql = "INSERT INTO customers (name, address, mobile , payment) VALUES (?,?,?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, mobile);
            preparedStatement.setDouble(4, payment);
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

    void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleUpdateCustomer() {
        Customer selectedCustomer = tableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tech/tech_info/UpdateCustomer.fxml"));
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

    public void updateCustomerInDatabase(Long id, String name, String address,Integer mobile, Double payment) {
        String sql = "UPDATE customers SET name = ?, address = ?, mobile = ?, payment = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setInt(3, mobile);
            preparedStatement.setDouble(4, payment);
            preparedStatement.setLong(5, id);
            preparedStatement.executeUpdate();
            loadCustomerData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

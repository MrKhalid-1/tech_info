package com.example.tech.tech_info.dao;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_NAME = "management.db";
    private static final String DB_PATH = "src/main/resources/sqlite";
    private static final String URL = "jdbc:sqlite:" + DB_PATH + File.separator + DB_NAME;

    private Connection connection;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public Connection connect() {
        try {
            File dbFile = new File(DB_PATH, DB_NAME);
            if (!dbFile.exists()) {
                createDatabaseAndTables();
            }
            connection = DriverManager.getConnection(URL);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return connection;
    }


    public static void createDatabaseAndTables() {
        File dbDir = new File(DB_PATH);
        if (!dbDir.exists()) {
            dbDir.mkdirs();
        }
        try (Connection connection = DriverManager.getConnection(URL)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT, " +
                    "mobile TEXT(12) NOT NULL UNIQUE, " +
                    "aadharCardNumber TEXT(14), " +
                    "payment REAL);");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS user (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username VARCHAR(50) NOT NULL, " +
                    "password VARCHAR(255) NOT NULL);");
            statement.executeUpdate("CREATE TABLE historyPayment (\n" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "    customerId INTEGER,\n" +
                    "    comment TEXT,\n" +
                    "    amount DOUBLE,\n" +
                    "    received_date DATE DEFAULT (DATE('now')),\n" +
                    "    image BLOB, imageName TEXT, imagePath TEXT,\n" +
                     "  FOREIGN KEY (customerId) REFERENCES customers(id) ON DELETE CASCADE);");

            System.out.println("Database and tables created successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String mobile = resultSet.getString("mobile");
                String aadharCardNumber = resultSet.getString("aadharCardNumber");
                Double payment = resultSet.getDouble("payment");
                System.out.println("Id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Address: " + address);
                System.out.println("Mobile: " + mobile);
                System.out.println("AadharCardNumber: " + aadharCardNumber);
                System.out.println("Payment: " + payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

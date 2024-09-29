package com.example.tech.tech_info.dao;

import java.sql.*;

public class DatabaseConnection {
//    private static final String URL = "jdbc:sqlite:sqlitTest/customers";
    private static final String URL = "jdbc:sqlite:sqliteTest/management.db";

    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to database at: " + URL);
        return DriverManager.getConnection(URL);
    }

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            while (resultSet.next()) {
                String Id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                Integer mobile = resultSet.getInt("mobile");
                String aadharCardNumber = resultSet.getString("aadharCardNumber");
                Double payment = resultSet.getDouble("payment");
                System.out.println("Id: " + Id);
                System.out.println("Name: " + name);
                System.out.println("Address: " + address);
                System.out.println("Mobile: " + mobile);
                System.out.println("AadharCardNumber: " + aadharCardNumber);
//                aadharCardNumber
                System.out.println("Payment: " + payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


/*
package com.example.tech.tech_info.dao;

import java.io.File;
import java.sql.*;

public class DatabaseConnection {
    private static final String DB_NAME = "management.db";
    private static final String DB_PATH = "sqliteTest"; // Adjust path as needed
    private static final String URL = "jdbc:sqlite:" + DB_PATH + File.separator + DB_NAME;

    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to database at: " + URL);
        return DriverManager.getConnection(URL);
    }

    public static void createDatabaseIfNotExists() {
        File dbFile = new File(DB_PATH, DB_NAME);
        if (!dbFile.exists()) {
            try (Connection connection = DriverManager.getConnection(URL)) {
                // Create tables
                Statement statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS customers ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "name TEXT NOT NULL, "
                        + "address TEXT, "
                        + "mobile INTEGER NOT NULL UNIQUE, "
                        + "aadharCardNumber TEXT(12), "
                        + "payment REAL);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS user ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username VARCHAR(50) NOT NULL, "
                        + "password VARCHAR(255) NOT NULL);");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS historyPayment ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "customerId INTEGER, "
                        + "comment TEXT, "
                        + "amount DOUBLE, "
                        + "received_date DATE DEFAULT (DATE('now')), "
                        + "FOREIGN KEY (customerId) REFERENCES customers(id));");

                System.out.println("Database and tables created successfully.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Database already exists at: " + dbFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        createDatabaseIfNotExists(); // Check and create the database if it doesn't exist

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM customers")) {

            while (resultSet.next()) {
                String Id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                Integer mobile = resultSet.getInt("mobile");
                String aadharCardNumber = resultSet.getString("aadharCardNumber");
                Double payment = resultSet.getDouble("payment");
                System.out.println("Id: " + Id);
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

 */



/*

package com.example.tech.tech_info.dao;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:sqliteTest/management.db";
    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            System.out.println("Connecting to database at: " + URL);
            connection = DriverManager.getConnection(URL);
        }
        return connection;
    }

    // Optional: Close the connection when the application is done
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

 */
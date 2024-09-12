

module com.example.tech.tech_info {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.tech.tech_info to javafx.fxml;
    exports com.example.tech.tech_info;
}
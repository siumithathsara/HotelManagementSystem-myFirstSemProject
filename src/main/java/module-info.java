module lk.ijse.hotelmanagementsystem_ijse {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires net.sf.jasperreports.core;






    opens lk.ijse.hotelmanagementsystem_ijse.controller to javafx.fxml;
    opens lk.ijse.hotelmanagementsystem_ijse.dto.tm to javafx.base;
    exports lk.ijse.hotelmanagementsystem_ijse;
    exports lk.ijse.hotelmanagementsystem_ijse.controller;
    exports lk.ijse.hotelmanagementsystem_ijse.dto;
}
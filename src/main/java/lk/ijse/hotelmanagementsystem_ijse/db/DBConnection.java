package lk.ijse.hotelmanagementsystem_ijse.db;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
public class DBConnection {
    private final String DB_URL = "jdbc:mysql://localhost:3306/hotel_management";
    private final String DB_USERNAME = "root";
    private final String DB_PASSWORD = "mysql";

    private static DBConnection dbc;
    private Connection conn;

    private DBConnection() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    public static DBConnection getInstance() throws SQLException {
        return (dbc==null) ? dbc = new DBConnection() : dbc;
    }

    public Connection getConnection() {
        return conn;
    }

}

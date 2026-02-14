package lk.ijse.hotelmanagementsystem_ijse.util;

import lk.ijse.hotelmanagementsystem_ijse.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {

    public static <T> T execute(String sql, Object... obj) throws SQLException {
        Connection conn = DBConnection.getInstance().getConnection();

        PreparedStatement ptsm = conn.prepareStatement(sql);

        for(int i=0; i<obj.length; i++) {
            ptsm.setObject(i + 1, obj[i]);
        }


        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            ResultSet results = ptsm.executeQuery();

            return (T)results;

        } else {

            int result = ptsm.executeUpdate();
            boolean results = result>0;

            return (T)(Boolean)results;

        }


    }

    // uses provided connection for transactions
    public static <T> T execute(Connection conn, String sql, Object... obj) throws SQLException {
        PreparedStatement ptsm = conn.prepareStatement(sql);

        for (int i = 0; i < obj.length; i++) {
            ptsm.setObject(i + 1, obj[i]);
        }

        if (sql.startsWith("SELECT") || sql.startsWith("select")) {
            ResultSet results = ptsm.executeQuery();
            return (T) results;
        } else {
            int result = ptsm.executeUpdate();
            boolean results = result>0;

            return (T)(Boolean)results;
        }

    }

}

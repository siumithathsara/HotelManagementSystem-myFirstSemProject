package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.RoomDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DashboardModel {

    public static int getCustomerCount()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT COUNT(*) FROM Customer"
        );
        return rs.next() ? rs.getInt(1) : 0;
    }

    public static int getAvailableRoomCount()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT COUNT(*) FROM Room_Details WHERE status='Available'"
        );
        return rs.next() ? rs.getInt(1) : 0;
    }

    public static int getTodayBookingCount()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT COUNT(*) FROM Booking WHERE DATE(created_at)=CURDATE()"
        );
        return rs.next() ? rs.getInt(1) : 0;
    }

    public static double getTodayRevenue()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT IFNULL(SUM(amount),0) FROM payment " +
                        "WHERE DATE(payment_date)=CURDATE() " +
                        "AND payment_status='Success'"
        );
        return rs.next() ? rs.getDouble(1) : 0.0;
    }


    public static List<RoomDetailsDTO> getRoomDetails()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT * FROM Room_Details ORDER BY room_id DESC"
        );

        List<RoomDetailsDTO> roomList = new ArrayList<>();

        while (rs.next()) {
            roomList.add(
                    new RoomDetailsDTO(
                            rs.getString("room_id"),
                            rs.getString("room_type"),
                            rs.getDouble("price_per_room"),
                            rs.getString("status")
                    )
            );
        }
        return roomList;
    }

    public static ResultSet getWeeklyBookings()
            throws SQLException, ClassNotFoundException {

        return CrudUtil.execute(
                """
                SELECT DAYNAME(created_at) AS day,
                       COUNT(*) AS count
                FROM Booking
                WHERE created_at >= CURDATE() - INTERVAL 6 DAY
                GROUP BY day
                ORDER BY MIN(created_at)
                """
        );
    }
}

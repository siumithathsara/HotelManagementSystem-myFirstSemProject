package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.dto.BookingDetailsDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class BookingDetailsModel {

    public boolean saveBookingDetails(BookingDetailsDTO bookingDetailsDTO) throws SQLException {


        return CrudUtil.execute("INSERT INTO Booking_Details (booking_id, room_id, check_in_date) VALUES (?,?,?)",
                bookingDetailsDTO.getBookingId(),
                bookingDetailsDTO.getRoomId(),
                bookingDetailsDTO.getCheckInDate());

    }

    public BookingDetailsDTO getBookingDetails(String bookingId) throws SQLException {


        ResultSet rs = CrudUtil.execute("SELECT * FROM Booking_Details WHERE booking_id = ?", bookingId);

        if (rs.next()) {
            return new BookingDetailsDTO(
                    rs.getString("booking_id"),
                    rs.getString("room_id"),
                    rs.getString("check_in_date")
            );
        }
        return null;
    }


public boolean updateBookingDetailsCheckOutDate(String bookingID, Date checkOutDate) throws SQLException {

    return CrudUtil.execute("UPDATE Booking_Details SET check_out_date = ? WHERE booking_id = ?",

            checkOutDate,
            bookingID

    );

}

    public boolean updateBookingDetails(BookingDetailsDTO bookingDetailsDTO) throws SQLException {

        return CrudUtil.execute("UPDATE Booking_Details SET room_id = ?, check_in_date = ? WHERE booking_id = ?",

                bookingDetailsDTO.getRoomId(),
                bookingDetailsDTO.getCheckInDate(),
                bookingDetailsDTO.getBookingId()

        );

    }

    public boolean deleteBookingDetails(String bookingId)
            throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM Booking_Details WHERE booking_id=?";
        return CrudUtil.execute(sql, bookingId);
    }

    public String getRoomIdByBookingId(String bookingId)
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute(
                "SELECT room_id FROM Booking_Details WHERE booking_id=?",
                bookingId
        );

        if (rs.next()) {
            return rs.getString("room_id");
        }
        return null;
    }

}

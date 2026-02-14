package lk.ijse.hotelmanagementsystem_ijse.model;

import lk.ijse.hotelmanagementsystem_ijse.db.DBConnection;
import lk.ijse.hotelmanagementsystem_ijse.dto.BookingDTO;
import lk.ijse.hotelmanagementsystem_ijse.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookingModel {

    private final BookingDetailsModel bookingDetailsModel= new BookingDetailsModel();
    private final RoomDetailsModel roomDetailsModel = new RoomDetailsModel();

    public boolean saveBooking(BookingDTO dto) throws Exception, ClassNotFoundException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);


            String sql = "INSERT INTO Booking (booking_id, customer_id, checkin_date, status) VALUES (?,?,?,?)";

            boolean isSaved =  CrudUtil.execute(
                    sql,
                    dto.getBooking_id(),
                    dto.getCustomer_id(),
                    dto.getBooking_date(),
                    dto.getStatus()
            );

            if (isSaved) {

                boolean isSavedBookingDetails = bookingDetailsModel.saveBookingDetails(dto.getBookingDetails());

                if (isSavedBookingDetails) {

                    roomDetailsModel.updateRoomStatus(dto.getBookingDetails().getRoomId(), "Booked");
                } else {
                    throw new Exception("Something went wrong when saving to the booking details table");
                }
            } else {
                throw new Exception("Something went wrong when saving to the booking table");
            }

            conn.commit();
            return true;

        } catch (Exception e) {

            conn.rollback();
            System.out.println(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
        return false;
    }
    public boolean updateBooking(BookingDTO dto)
            throws SQLException, ClassNotFoundException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);


            String sql = "UPDATE Booking SET customer_id = ?, checkin_date = ?, status = ? WHERE booking_id = ?";

            boolean isSaved =  CrudUtil.execute(
                    sql,
                    dto.getCustomer_id(),
                    dto.getBooking_date(),
                    dto.getStatus(),
                    dto.getBooking_id()
            );


            if (isSaved) {

                boolean isSavedBookingDetails = bookingDetailsModel.updateBookingDetails(dto.getBookingDetails());

                if (isSavedBookingDetails) {

                    roomDetailsModel.updateRoomStatus(dto.getBookingDetails().getRoomId(), "Available");
                } else {
                    throw new Exception("Something went wrong when saving to the booking details table");
                }
            } else {
                throw new Exception("Something went wrong when saving to the booking table");
            }

            conn.commit();
            return true;

        } catch (Exception e) {

            conn.rollback();
            System.out.println(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
        return false;
    }

    public boolean deleteBooking(String bookingId)
            throws SQLException, ClassNotFoundException {

        Connection conn = DBConnection.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);


            String roomId = bookingDetailsModel.getRoomIdByBookingId(bookingId);

            if (roomId == null) {
                throw new SQLException("Room ID not found for booking: " + bookingId);
            }


            boolean isDetailsDeleted =
                    bookingDetailsModel.deleteBookingDetails(bookingId);

            if (!isDetailsDeleted) {
                throw new SQLException("Booking details delete failed");
            }


            boolean isBookingDeleted = CrudUtil.execute(
                    "DELETE FROM Booking WHERE booking_id=?",
                    bookingId
            );

            if (!isBookingDeleted) {
                throw new SQLException("Booking delete failed");
            }

            boolean isRoomUpdated =
                    roomDetailsModel.updateRoomStatus(roomId, "Available");

            if (!isRoomUpdated) {
                throw new SQLException("Room status update failed");
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            conn.rollback();
            System.out.println("DELETE ERROR: " + e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }
        return false;
    }



    public BookingDTO searchBooking(String bookingId)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Booking WHERE booking_id=?";
        ResultSet rs = CrudUtil.execute(sql, bookingId);

        if (rs.next()) {
            return new BookingDTO(
                    rs.getString("booking_id"),
                    rs.getString("customer_id"),
                    rs.getDate("checkin_date"),
                    //rs.getTime("booking_time").toLocalTime(),
                    rs.getString("special_note"),
                    rs.getString("status"),
                    //rs.getString("created_by"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
        return null;
    }

    public List<BookingDTO> getAllBookings()
            throws SQLException, ClassNotFoundException {

        ResultSet rs = CrudUtil.execute("SELECT * FROM Booking ORDER BY booking_id DESC");
        List<BookingDTO> bookingList = new ArrayList<>();

        while (rs.next()) {
            bookingList.add(
                    new BookingDTO(
                            rs.getString("booking_id"),
                            rs.getString("customer_id"),
                            rs.getDate("checkin_date"),
                            rs.getString("special_note"),
                            rs.getString("status"),
                           // rs.getString("created_by"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    )
            );
        }
        return bookingList;
    }



    public boolean updateBookingStatus(String bookingId, String status)
            throws SQLException, ClassNotFoundException {

        String sql = "UPDATE Booking SET status=? WHERE booking_id=?";
        return CrudUtil.execute(sql, status, bookingId);
    }

    public List<BookingDTO> getBookingsByDate(String date)
            throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM Booking WHERE booking_date=?";
        ResultSet rs = CrudUtil.execute(sql, date);

        List<BookingDTO> list = new ArrayList<>();
        while (rs.next()) {
            list.add(
                    new BookingDTO(
                            rs.getString("booking_id"),
                            rs.getString("customer_id"),
                            rs.getDate("booking_date"),
                            rs.getTime("booking_time").toLocalTime(),
                            rs.getString("special_note"),
                            rs.getString("status"),
                            rs.getString("created_by"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    )
            );
        }
        return list;
    }

    public String generateNextBookingId() throws SQLException {
        ResultSet rs = CrudUtil.execute(
                "SELECT booking_id FROM Booking ORDER BY booking_id DESC LIMIT 1"
        );

        if (rs.next()) {
            String lastId = rs.getString(1); // E.g., C005
            int number = Integer.parseInt(lastId.substring(1));
            number++;
            return String.format("B%03d", number);
        }
        return "B001";
    }


    public boolean placeBooking(String bookingId, String customerId, String roomId, Date checkInDate, Date checkOutDate, double totalPrice) throws SQLException{

        Connection conn = DBConnection.getInstance().getConnection();

        try {

            conn.setAutoCommit(false);

            String sql = "UPDATE Booking SET checkout_date = ? WHERE booking_id = ?";

            boolean isUpdate =  CrudUtil.execute(
                    sql,
                    checkOutDate,
                    bookingId
            );

            if (isUpdate) {

                boolean isUpdateBookingDetails = bookingDetailsModel.updateBookingDetailsCheckOutDate(bookingId, checkOutDate);

                if (isUpdateBookingDetails) {

                    roomDetailsModel.updateRoomStatus(roomId, "Available");

                } else {
                    throw new Exception("Something went wrong when update to the checkout date to the booking details table");
                }
            }  else {
                throw new Exception("Something went wrong when update to the checkout date in booking table");
            }

            conn.commit();
            return true;

        } catch (Exception e) {

            conn.rollback();
            System.out.println(e.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }

        return false;
    }
}

package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.*;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookingDTO {

    private String booking_id;
    private String customer_id;
    private Date booking_date;
    private LocalTime booking_time;
    private String special_note;
    private String status;
    private String created_by;
    private BookingDetailsDTO bookingDetails;

    private LocalDateTime created_at;public BookingDTO(String booking_id, String customer_id, Date booking_date, String status, BookingDetailsDTO bookingDetails) {
        this.booking_id = booking_id;
        this.customer_id = customer_id;
        this.booking_date = booking_date;
        this.status = status;
        this.bookingDetails = bookingDetails;
    }

    public BookingDTO(String booking_id, String customer_id, Date booking_date, LocalTime booking_time, String special_note, String status, String created_by, LocalDateTime created_at) {
        this.booking_id = booking_id;
        this.customer_id = customer_id;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.special_note = special_note;
        this.status = status;
        this.created_by = created_by;
        this.created_at = created_at;
    }


    public BookingDTO(String bookingId, String customerId, java.sql.Date checkinDate, String specialNote, String status, String createdBy, LocalDateTime createdAt) {
        this.booking_id = bookingId;
        this.customer_id = customerId;
        this.booking_date = checkinDate;
        this.special_note = specialNote;
        this.status = status;
        this.created_by = created_by;
        this.created_at = created_at;

    }

    public BookingDTO(String bookingId, String customerId, java.sql.Date checkinDate, String specialNote, String status, LocalDateTime createdAt) {
        this.booking_id = bookingId;
        this.customer_id = customerId;
        this.booking_date = checkinDate;
        this.special_note = specialNote;
        this.status = status;
        this.created_by = created_by;
       // this.created_at = created_at;
    }
}

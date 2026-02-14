package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class BookingDetailsDTO {
    private String bookingId;
    private String roomId;
    private String checkInDate;
    private String checkOutDate;


    public BookingDetailsDTO(String bookingId, String roomId, String checkInDate) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
    }
}

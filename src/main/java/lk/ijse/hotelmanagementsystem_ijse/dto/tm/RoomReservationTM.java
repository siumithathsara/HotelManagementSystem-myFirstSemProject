package lk.ijse.hotelmanagementsystem_ijse.dto.tm;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class RoomReservationTM {

    private String bookingId;
    private String customerName;
    private String roomId;
    private String roomType;
    private double pricePerNight;
    private Date checkInDate;
    private Date checkOutDate;
    private double totalPrice;

}

package lk.ijse.hotelmanagementsystem_ijse.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class BookingTM {

    private String bookingId;
    private String roomId;
    private String customerName;
    private String roomType;
    private Double pricePerNight;
    private Date checkInDate;
    private String status;
}

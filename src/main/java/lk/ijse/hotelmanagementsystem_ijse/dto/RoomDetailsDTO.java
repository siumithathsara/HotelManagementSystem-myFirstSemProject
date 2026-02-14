package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoomDetailsDTO {

    private String roomId;
    private String roomType;
    private double pricePerRoom;
    private String status;
}

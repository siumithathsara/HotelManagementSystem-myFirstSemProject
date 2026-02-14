package lk.ijse.hotelmanagementsystem_ijse.dto.tm;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RoomTM {
    private String roomId;
    private String roomType;
    private double price;
    private String status;


}

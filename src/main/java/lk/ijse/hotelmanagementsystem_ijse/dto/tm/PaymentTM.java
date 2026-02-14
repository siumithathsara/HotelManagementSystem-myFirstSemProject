package lk.ijse.hotelmanagementsystem_ijse.dto.tm;


import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString

public class PaymentTM {
    private String paymentId;
    private String status;
    private Date paymentDate;
    private double amount;
    private String method;


}

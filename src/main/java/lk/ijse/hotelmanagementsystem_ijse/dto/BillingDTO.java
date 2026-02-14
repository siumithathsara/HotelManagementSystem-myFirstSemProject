package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BillingDTO {

    private String bill_id;
    private String reservation_id;
    private double total_amount;
    private double paid_amount;
    private double balance_amount;
    private LocalDate billing_date;
    private String status;
}

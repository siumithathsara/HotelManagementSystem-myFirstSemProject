package lk.ijse.hotelmanagementsystem_ijse.dto;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentDto {

    private String payment_id;
    private String bill_id;
    private LocalDate payment_date;
    private double amount;
    private String payment_method;
    private String payment_status;
    private String received_by;

    public PaymentDto(String payment_id, LocalDate payment_date, double amount, String payment_method, String payment_status) {
        this.payment_id = payment_id;
        this.payment_date = payment_date;
        this.amount = amount;
        this.payment_method = payment_method;
        this.payment_status = payment_status;
    }
}

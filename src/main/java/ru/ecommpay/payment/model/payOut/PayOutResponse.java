package ru.ecommpay.payment.model.payOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by
 * Mher Petrosyan
 * Email mher13.02.94@gmail.ru
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayOutResponse {
    private String status;
    private String request_id;
    private Integer project_id;
    private String payment_id;
}

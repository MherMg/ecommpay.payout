package ru.ecommpay.payment.model.getCard;

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
public class Additional {
    private String country;
    private String phone;
    private String email;
    private Card card;
}

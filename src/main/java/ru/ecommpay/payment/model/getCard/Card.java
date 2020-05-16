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
public class Card {
    private String expiry;
    private String holder;
    private String type;
}

package ru.ecommpay.payment.model.callback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class TokenData {
    private General general;
    private Request request;
    private Customer customer;
    private String tokenStatus;
    private String tokenCreatedAt;
    private String token;
}

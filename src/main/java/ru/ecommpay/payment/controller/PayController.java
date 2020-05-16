package ru.ecommpay.payment.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ecommpay.payment.model.getCard.GetCardTokenResponse;
import ru.ecommpay.payment.model.payOut.PayOutResponse;
import ru.ecommpay.payment.model.payOut.Payment;
import ru.ecommpay.payment.service.EcommpayService;

import javax.servlet.http.HttpServletRequest;

import static org.apache.logging.log4j.util.Strings.isBlank;

@RestController
public class PayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcommpayService.class);

    private final EcommpayService ecommpayService;


    @Autowired
    public PayController(
            EcommpayService ecommpayService) {
        this.ecommpayService = ecommpayService;
    }


    @ApiResponses({
            @ApiResponse(code = 400, message = "AMOUNT_IS_EMPTY,CURRENCY_IS_EMPTY," +
                    "PAYMENT_ID_IS_EMPTY,TOKEN_IS_EMPTY"),
            @ApiResponse(code = 200, message = "", response = PayOutResponse.class)})
    @PostMapping(value = "/api/peyOut")
    public HttpEntity peyOut(@RequestBody RequestPayOut requestPayOut) {


        if (requestPayOut.amount == 0) {
            return new ResponseEntity<>(ErrorCode.AMOUNT_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.currency)) {
            return new ResponseEntity<>(ErrorCode.CURRENCY_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.paymentId)) {
            return new ResponseEntity<>(ErrorCode.PAYMENT_ID_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.token)) {
            return new ResponseEntity<>(ErrorCode.TOKEN_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.signature)) {
            return new ResponseEntity<>(ErrorCode.SIGNATURE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.customerId)) {
            return new ResponseEntity<>(ErrorCode.CUSTOMER_ID_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestPayOut.ipAddress)) {
            return new ResponseEntity<>(ErrorCode.IP_ADDRESS_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        Payment payment = new Payment(requestPayOut.amount, requestPayOut.currency);
        PayOutResponse payOutResponse = ecommpayService.payOut(payment, requestPayOut.paymentId, requestPayOut.token, requestPayOut.signature, requestPayOut.customerId, requestPayOut.ipAddress);

        LOGGER.debug("PayOut - created,pay out - : [{}]", payOutResponse);

        return new ResponseEntity<>(payOutResponse, HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "CUSTOMER_ID_IS_EMPTY,TOKEN_IS_EMPTY"),
            @ApiResponse(code = 200, message = "", response = GetCardTokenResponse.class)})
    @PostMapping(value = "/api/getCardByToken")
    public HttpEntity getCardByToken(@RequestBody RequestGetCard requestGetCard) {


        if (isBlank(requestGetCard.token)) {
            return new ResponseEntity<>(ErrorCode.TOKEN_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestGetCard.customerId)) {
            return new ResponseEntity<>(ErrorCode.CUSTOMER_ID_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestGetCard.signature)) {
            return new ResponseEntity<>(ErrorCode.SIGNATURE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        GetCardTokenResponse cardData = ecommpayService.getCard(requestGetCard.token, requestGetCard.customerId, requestGetCard.signature);

        LOGGER.debug("CardData : [{}]", cardData);

        return new ResponseEntity<>(cardData, HttpStatus.OK);
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "CUSTOMER_ID_IS_EMPTY,TOKEN_IS_EMPTY," +
                    "SIGNATURE_IS_EMPTY")})
    @PostMapping(value = "/api/revokeCardToken")
    public HttpEntity revokeCardToken(@RequestBody RequestGetCard requestGetCard) {


        if (isBlank(requestGetCard.token)) {
            return new ResponseEntity<>(ErrorCode.TOKEN_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestGetCard.customerId)) {
            return new ResponseEntity<>(ErrorCode.CUSTOMER_ID_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        if (isBlank(requestGetCard.signature)) {
            return new ResponseEntity<>(ErrorCode.SIGNATURE_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }
        String status = ecommpayService.revokeToken(requestGetCard.token, requestGetCard.customerId, requestGetCard.signature);


        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    private static class RequestPayOut {
        public Integer amount;
        public String currency;
        public String customerId;
        public String ipAddress;
        public String paymentId;
        public String token;
        public String signature;

    }

    private static class RequestGetCard {
        public String token;
        public String signature;
        public String customerId;
    }

    private enum ErrorCode {
        PAYMENT_ID_IS_EMPTY,
        CURRENCY_IS_EMPTY,
        TOKEN_IS_EMPTY,
        CUSTOMER_ID_IS_EMPTY,
        SIGNATURE_IS_EMPTY,
        IP_ADDRESS_IS_EMPTY,
        AMOUNT_IS_EMPTY


    }

}

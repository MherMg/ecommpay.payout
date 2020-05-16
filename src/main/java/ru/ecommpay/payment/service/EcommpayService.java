package ru.ecommpay.payment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.ecommpay.payment.model.getCard.Customer;
import ru.ecommpay.payment.model.getCard.GetCardByTokenRequest;
import ru.ecommpay.payment.model.getCard.GetCardTokenResponse;
import ru.ecommpay.payment.model.payOut.General;
import ru.ecommpay.payment.model.payOut.PayOutRequest;
import ru.ecommpay.payment.model.payOut.PayOutResponse;
import ru.ecommpay.payment.model.payOut.Payment;

/**
 * Created by
 * Mher Petrosyan
 * Email mher13.02.94@gmail.ru
 */
@Service
public class EcommpayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcommpayService.class);

    //    private final TokenDataRepository tokenDataRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${project.id}")
    private Integer projectId;
    @Value("${url.card.by.token}")
    private String urlCardByToken;
    @Value("${url.pay.out}")
    private String urlPayOut;
    @Value("${chaevie.callback}")
    private String chaevieCallback;
    @Value("${url.revoke.token}")
    private String urlRevokeToken;

//    @Autowired
//    public EcommpayService(TokenDataRepository tokenDataRepository) {
//        this.tokenDataRepository = tokenDataRepository;
//    }

    public GetCardTokenResponse getCard(String token, String customerId, String signature) {

        GetCardByTokenRequest getCardByTokenRequest = new GetCardByTokenRequest();
        getCardByTokenRequest.setToken(token);
        Customer customer = new Customer(projectId, customerId, signature);
        getCardByTokenRequest.setCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GetCardByTokenRequest> entity = new HttpEntity<>(getCardByTokenRequest, headers);


        return restTemplate.exchange(urlCardByToken, HttpMethod.POST, entity, GetCardTokenResponse.class).getBody();
    }

    public PayOutResponse payOut(Payment payment, String paymentId, String token, String signature, String customerId, String ipAddress) {

        PayOutRequest payOutRequest = new PayOutRequest();
        ru.ecommpay.payment.model.payOut.Customer customer = new ru.ecommpay.payment.model.payOut.Customer();
        customer.setId(customerId);
        customer.setIp_address(ipAddress);
        payOutRequest.setCustomer(customer);
        General general = new General();
        general.setPayment_id(paymentId);
        general.setProject_id(projectId);
        general.setSignature(signature);
        payOutRequest.setGeneral(general);

        payOutRequest.setPayment(payment);
        payOutRequest.setToken(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PayOutRequest> entity = new HttpEntity<>(payOutRequest, headers);


        return restTemplate.exchange(urlPayOut, HttpMethod.POST, entity, PayOutResponse.class).getBody();
    }

//    public void saveToken(TokenData tokenData) {
//        tokenDataRepository.save(tokenData);
//    }


//    //    not using now
//    public TokenData findByCustomerId(String customerId) {
//        return tokenDataRepository.findByGeneral_Customer_Id(customerId);
//    }

    public void sendTokenWithCallBack(String tokenData) {

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(tokenData, headers);


            restTemplate.exchange(chaevieCallback, HttpMethod.POST, entity, String.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public String revokeToken(String token, String customerId, String signature) {
        GetCardByTokenRequest requestForRevokeToken = new GetCardByTokenRequest();
        requestForRevokeToken.setToken(token);
        Customer customer = new Customer(projectId, customerId, signature);
        requestForRevokeToken.setCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GetCardByTokenRequest> entity = new HttpEntity<>(requestForRevokeToken, headers);


        return restTemplate.exchange(urlRevokeToken, HttpMethod.POST, entity, String.class).getBody();
    }
}

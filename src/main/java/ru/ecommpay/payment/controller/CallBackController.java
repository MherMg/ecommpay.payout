package ru.ecommpay.payment.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.ecommpay.payment.service.EcommpayService;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CallBackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EcommpayService.class);
    private final EcommpayService ecommpayService;


    @Autowired
    public CallBackController(
            EcommpayService ecommpayService) {
        this.ecommpayService = ecommpayService;
    }

    @ApiIgnore
    @PostMapping(value = "/api/callback")
    public void callback(@Valid @RequestBody String tokenData, BindingResult result) {
        LOGGER.debug("CallBack - token data response : [{}]", tokenData);
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError allError : allErrors) {
                LOGGER.error("CallBack - response errors: [{}]", allError.toString());
            }
        } else {
            ecommpayService.sendTokenWithCallBack(tokenData);
            LOGGER.debug("CallBack - token data send : [{}]", tokenData);
        }


    }

}

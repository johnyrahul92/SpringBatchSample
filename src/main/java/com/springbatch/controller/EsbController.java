package com.springbatch.controller;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.springbatch.beans.ErrorResponseBean;
import com.springbatch.beans.KycRequestBean;
import com.springbatch.exception.ErrorMessageHandling;
import com.springbatch.exception.PortalException;

@RestController
@RequestMapping("/esb")
public class EsbController {

	private static final Logger LOGGER = LogManager.getLogger(EsbController.class);
	
	@Autowired
	ErrorMessageHandling errorMessageHandling;
	
	@PostMapping("/initiate")
	public String esbCall(@RequestBody KycRequestBean kycRequestBean) throws PortalException {
        try {
        	LOGGER.info("ESB call received with data :- ");
        	LOGGER.info("UniqueId : "+kycRequestBean.getUniqueId());
        	LOGGER.info("KYC data : "+kycRequestBean.getKycData());
        } catch (Exception e) {
            throw new PortalException("KYC102");
        }
        return "Success";
    }

    @ExceptionHandler(PortalException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponseBean> portalErrorException(PortalException ex) throws IOException {
        ErrorResponseBean portalErrorResponseBean = errorMessageHandling.errorHandlingResponse(ex.getErrCode());
        return new ResponseEntity<>(portalErrorResponseBean, HttpStatus.BAD_REQUEST);
    }
}

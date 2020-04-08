package com.springbatch.exception;

import com.springbatch.beans.ErrorResponseBean;
import com.springbatch.beans.ErrorResponseListBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("errorMessageHandling")
public class ErrorMessageHandling {

    @Autowired
    private Environment env;

    private String errorMsgPrefix = "error.message.";

    public ErrorResponseBean errorHandlingResponse(String code) {

        List<ErrorResponseListBean> responeList = new ArrayList<>();
        ErrorResponseListBean errorResponseListBean = new ErrorResponseListBean();
        ErrorResponseBean bean = new ErrorResponseBean();
        String errorMessage = env.getProperty(errorMsgPrefix+code);
        if (errorMessage != null) {
            errorResponseListBean.setCode(code);
            errorResponseListBean.setMessage(errorMessage);
            responeList.add(errorResponseListBean);
            bean.setErrors(responeList);
            return bean;
        } else {
            errorResponseListBean.setCode(code);
            errorResponseListBean.setMessage("Error in fetching data.Please try again.");
            responeList.add(errorResponseListBean);
            bean.setErrors(responeList);
            return bean;
        }
    }
}

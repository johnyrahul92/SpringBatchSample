package com.springbatch.beans;

import java.util.List;

public class ErrorResponseBean {

    private List<ErrorResponseListBean> errors;

    public List<ErrorResponseListBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorResponseListBean> errors) {
        this.errors = errors;
    }
}

package com.springbatch.exception;

public class PortalException extends Exception {
    private String errCode;

    public PortalException(String errCode) {
        this.errCode=errCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}

package com.bitfty.mall.exception;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -3197616652643414121L;

    private String msg;

    private ServiceCode serviceCode;

    public ServiceException() {

    }

    public ServiceException(ServiceCode infoCode) {
        super(infoCode.getMsg());
        this.serviceCode = infoCode;
        this.msg = infoCode.getMsg();
    }

    public ServiceException(ServiceCode infoCode, String msg) {
        super(msg);
        this.serviceCode = infoCode;
        this.msg = msg;
    }

    public ServiceException(ServiceCode infoCode, String msg, Throwable e) {
        super(msg, e);
        this.serviceCode = infoCode;
        this.msg = msg;
    }

    public ServiceCode getInfoCode() {
        return serviceCode;
    }

    public String getMsg() {
        return msg;
    }
}

/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

/**
  *
 * @author chenhetong(chenhetong@baidu.com)
 * 
 */
public class BaiduException extends Exception {

    private static final long serialVersionUID = -8309515227501598366L;

    private String errorCode;

    private String errorMsg;

    public BaiduException() {
        super();
    }

    public BaiduException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public BaiduException(String detailMessage) {
        super(detailMessage);
    }

    public BaiduException(Throwable throwable) {
        super(throwable);
    }

    public BaiduException(String errorCode, String errorDesp) {
        this.errorCode = errorCode;
        this.errorMsg = errorDesp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDesp() {
        return errorMsg;
    }

    public void setErrorDesp(String errorDesp) {
        this.errorMsg = errorDesp;
    }

    @Override
    public String toString() {
        return "BaiduException [errorCode=" + errorCode + ", errorDesp=" + errorMsg + "]";
    }

}

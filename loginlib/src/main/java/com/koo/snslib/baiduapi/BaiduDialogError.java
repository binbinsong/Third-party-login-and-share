/**
 * Copyright (c) 2011 Baidu.com, Inc. All Rights Reserved
 */
package com.koo.snslib.baiduapi;

/**
  *
 * @author chenhetong(chenhetong@baidu.com)
 * 
 */
public class BaiduDialogError extends Exception {

    private static final long serialVersionUID = 1529106452635370329L;

    private int errorCode;

    private String failingUrl;

    public BaiduDialogError(String message, int errorCode, String failingUrl) {
        super(message);
        this.errorCode = errorCode;
        this.failingUrl = failingUrl;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getFailingUrl() {
        return failingUrl;
    }

}

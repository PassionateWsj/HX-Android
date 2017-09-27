package com.intfocus.hx.net;

/**
 * Created by CANC on 2017/8/11.
 */

public class HttpStateException extends Error {
    private int errorCode;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}

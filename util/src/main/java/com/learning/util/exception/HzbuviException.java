package com.learning.util.exception;

/**
 * Created by taylor on 9/5/16.
 * twitter: @taylorwang789
 */
public class HzbuviException extends Exception{

    private String code;
    private String msg;

    public HzbuviException(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

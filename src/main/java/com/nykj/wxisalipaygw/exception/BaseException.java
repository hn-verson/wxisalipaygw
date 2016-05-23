package com.nykj.wxisalipaygw.exception;

/**
 * Created by Verson on 2016/5/23.
 */
public class BaseException extends Exception {
    protected final int code;
    public BaseException(int code) {
        super();
        this.code = code;
    }

    public BaseException(int code,String message) {
        super(message);
        this.code = code;
    }

    public BaseException(int code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(int code,Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
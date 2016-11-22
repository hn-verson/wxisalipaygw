package com.nykj.wxisalipaygw.exception;

/**
 * Created by Verson on 2016/5/23.
 */
public class ApiCallException extends BaseException {
    public ApiCallException(int code) {
        super(code);
    }

    public ApiCallException(int code, String message) {
        super(code, message);
    }

    public ApiCallException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ApiCallException(int code, Throwable cause) {
        super(code, cause);
    }
}
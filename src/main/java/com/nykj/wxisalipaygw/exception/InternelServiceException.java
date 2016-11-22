package com.nykj.wxisalipaygw.exception;

/**
 * Created by Verson on 2016/5/23.
 */
public class InternelServiceException extends BaseException {
    public InternelServiceException(int code) {
        super(code);
    }

    public InternelServiceException(int code, String message) {
        super(code, message);
    }

    public InternelServiceException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public InternelServiceException(int code, Throwable cause) {
        super(code, cause);
    }
}
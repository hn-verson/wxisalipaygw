package com.nykj.wxisalipaygw.exception;

/**
 * Created by Verson on 2016/5/23.
 */
public class ArgumentException extends BaseException {
    public ArgumentException(int code) {
        super(code);
    }

    public ArgumentException(int code, String message) {
        super(code, message);
    }

    public ArgumentException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ArgumentException(int code, Throwable cause) {
        super(code, cause);
    }
}
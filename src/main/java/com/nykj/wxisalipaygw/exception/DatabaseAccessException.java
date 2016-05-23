package com.nykj.wxisalipaygw.exception;

/**
 * Created by Verson on 2016/5/23.
 */
public class DatabaseAccessException extends BaseException {
    public DatabaseAccessException(int code) {
        super(code);
    }

    public DatabaseAccessException(int code, String message) {
        super(code, message);
    }

    public DatabaseAccessException(int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public DatabaseAccessException(int code, Throwable cause) {
        super(code, cause);
    }
}
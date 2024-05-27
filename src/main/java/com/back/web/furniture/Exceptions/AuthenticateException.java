package com.back.web.furniture.Exceptions;

public class AuthenticateException extends BaseException {
    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }
}

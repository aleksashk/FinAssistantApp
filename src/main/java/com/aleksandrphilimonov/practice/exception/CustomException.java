package com.aleksandrphilimonov.practice.exception;

public class CustomException extends RuntimeException {
    public CustomException(Throwable cause) {
        super(cause);
    }
}

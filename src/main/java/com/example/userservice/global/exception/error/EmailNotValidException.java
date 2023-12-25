package com.example.userservice.global.exception.error;

public class EmailNotValidException extends  RuntimeException {

    public EmailNotValidException() {
        super();
    }
    public EmailNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
    public EmailNotValidException(String message) {
        super(message);
    }
    public EmailNotValidException(Throwable cause) {
        super(cause);
    }
}

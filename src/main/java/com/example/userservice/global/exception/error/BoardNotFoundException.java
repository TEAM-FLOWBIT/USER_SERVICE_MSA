package com.example.userservice.global.exception.error;

public class BoardNotFoundException extends RuntimeException {
    public BoardNotFoundException() {
        super();
    }
    public BoardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public BoardNotFoundException(String message) {
        super(message);
    }
    public BoardNotFoundException(Throwable cause) {
        super(cause);
    }

}
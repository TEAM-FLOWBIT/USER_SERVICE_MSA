package com.example.userservice.global.exception.error;

public class BoardCommentNotFoundException extends RuntimeException{
    public BoardCommentNotFoundException() {
        super();
    }
    public BoardCommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public BoardCommentNotFoundException(String message) {
        super(message);
    }
    public BoardCommentNotFoundException(Throwable cause) {
        super(cause);
    }

}

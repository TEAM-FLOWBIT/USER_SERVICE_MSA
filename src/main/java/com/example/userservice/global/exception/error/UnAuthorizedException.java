package com.example.userservice.global.exception.error;

public class UnAuthorizedException extends RuntimeException{
        public UnAuthorizedException() {
            super();
        }
        public UnAuthorizedException(String message, Throwable cause) {
            super(message, cause);
        }
        public UnAuthorizedException(String message) {
            super(message);
        }
        public UnAuthorizedException(Throwable cause) {
            super(cause);
        }
}

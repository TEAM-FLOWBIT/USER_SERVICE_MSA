package com.example.userservice.global.exception.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private int status;
}
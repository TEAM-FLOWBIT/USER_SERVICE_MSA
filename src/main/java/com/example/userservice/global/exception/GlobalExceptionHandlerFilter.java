package com.example.userservice.global.exception;

import com.example.userservice.global.exception.error.NotFoundAccountException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            // Handle exceptions and set the error response
            handleException(ex, response);
        }
    }



    private void handleException(Exception ex, HttpServletResponse response) throws IOException {
        if (ex instanceof NotFoundAccountException) {
            setErrorResponse(response, ErrorCode.NOT_FOUND_ACCOUNT_EXCEPTION);
        } else if (ex instanceof ExpiredJwtException) {
            setErrorResponse(response, ErrorCode.INVALID_TOKEN_EXCEPTION);
        } else {
            // Handle other exceptions as needed
            setErrorResponse(response, ErrorCode.UnAuthorizedException); // Create a generic error code
        }
    }

    private void setErrorResponse(
            HttpServletResponse response,
            ErrorCode errorCode
    ) {
        ObjectMapper objectMapper = new ObjectMapper().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
        try {
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    public static class ErrorResponse {
        private final String code;
        private final String message;
    }
}
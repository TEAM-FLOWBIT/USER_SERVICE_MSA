package com.example.userservice.global.exception;

import com.example.userservice.global.exception.dto.CommonResponse;
import com.example.userservice.global.exception.dto.ErrorResponse;
import com.example.userservice.global.exception.error.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.net.BindException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 회원가입 시, 회원 계정 정보가 중복 되었을때
     */
    @ExceptionHandler(DuplicateAccountException.class)
    protected ResponseEntity<?> handleDuplicateAccountException(DuplicateAccountException ex) {
        ErrorCode errorCode = ErrorCode.DUPLICATE_ACCOUNT_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     *
     */
    @ExceptionHandler(BoardNotFoundException.class)
    protected ResponseEntity<?> handleBoardNotFoundException(BoardNotFoundException ex) {
        ErrorCode errorCode = ErrorCode.BOARD_NOT_FOUND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<?> handleUnAuthorizedException(UnAuthorizedException ex) {
        ErrorCode errorCode = ErrorCode.UnAuthorizedException;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 계정 주요 정보의 암호화 과정이 실패 했을때
     */
//    @ExceptionHandler(EncryptException.class)
//    protected ResponseEntity<CommonResponse> handleEncryptException(EncryptException ex) {
//        ErrorCode errorCode = ErrorCode.ENCRYPT_FAILED_EXCEPTION;
//
//        ErrorResponse error = ErrorResponse.builder()
//                .status(errorCode.getStatus().value())
//                .message(errorCode.getMessage())
//                .code(errorCode.getCode())
//                .build();
//
//        CommonResponse response = CommonResponse.builder()
//                .success(false)
//                .error(error)
//                .build();
//
//        return new ResponseEntity<>(response, errorCode.getStatus());
//    }

    /**
     *
     * 이메일 유효시간이 지났을 경우
     */
    @ExceptionHandler(EmailNotValidException.class)
    protected ResponseEntity<CommonResponse> emailValidationException(EmailNotValidException ex) {
        ErrorCode errorCode = ErrorCode.EMAIL_VALID_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 리퀘스트 파라미터 바인딩이 실패했을때
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<CommonResponse> handleRequestParameterBindException(BindException ex) {

        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 사용자 인증이 실패했을때
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<CommonResponse> handleAuthenticationException() {
        ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED_EXCEPTION;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    /**
     * 비밀번호가 일치않았을 때
     */
    @ExceptionHandler(PasswordNotMatchException.class)
    protected ResponseEntity<CommonResponse> PasswordNotMatchException() {
        ErrorCode errorCode = ErrorCode.PASSWORD_NOT_MATCH;

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }



    /**
     * 계정을 찿을 수 없을 때
     */
    @ExceptionHandler(NotFoundAccountException.class)
    protected ResponseEntity<CommonResponse> handleNotFoundAccountException(NotFoundAccountException ex) {

        log.info("들어오나요");
        ErrorCode errorCode = ErrorCode.NOT_FOUND_ACCOUNT_EXCEPTION;
        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     *
     * 유효성검사에 실패하는
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(BindingResult bindingResult,MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ErrorCode errorCode = ErrorCode.REQUEST_PARAMETER_BIND_EXCEPTION;

        List<String> errorMessages = fieldErrors.stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorMessages.toString())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }


    /**
     *  주로 파일 업로드나 멀티파트 요청에서 파트나 매개변수가 누락된 경우에 해당 예외
     */

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> missingServletRequestPartException(MissingServletRequestPartException exception) {
//        Sentry.captureException(exception);
        log.error("MissingServletRequestPartException = {}", exception);
        return ResponseEntity.badRequest().body("MissingServletRequestPartException");
    }
    /**
     *
     * 유효성검사 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        log.error("MethodArgumentTypeMismatchException = {}", exception);
        return ResponseEntity.badRequest().body("잘못된 형식의 값입니다.");
    }
}
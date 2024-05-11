package sideproject.gugumo.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sideproject.gugumo.exception.exception.DuplicateEmailException;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler()
    public ApiResponse<String> duplicateEmailExceptionHandler(DuplicateEmailException e) {
        log.error("[duplicateEmailExceptionHandler] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidExceptionHandler] ex : " + e.getMessage());
        return ApiResponse.createFail("입력이 올바르지 못합니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> MissingRequestHeaderExceptionHandler(MissingRequestHeaderException e) {
        log.error("[MissingRequestHeaderExceptionHandler] ex : " + e.getMessage());
        return ApiResponse.createFail("권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> NoAuthorizationExceptionHandler(NoAuthorizationException e) {
        log.error("[NoAuthorizationExceptionHandler] ex : " + e.getMessage());
        return ApiResponse.createFail(e.getMessage());
    }
}

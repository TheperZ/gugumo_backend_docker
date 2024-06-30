package sideproject.gugumo.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sideproject.gugumo.exception.exception.DuplicateEmailException;
import sideproject.gugumo.exception.exception.DuplicateNicknameException;
import sideproject.gugumo.exception.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.UserNotFoundException;
import sideproject.gugumo.response.ApiResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler()
    public ApiResponse<String> duplicateEmailExceptionHandler(DuplicateEmailException e) {
        String exceptionMessage = e.getMessage();
        log.error("[duplicateEmailExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidExceptionHandler] ex : 입력이 올바르지 못합니다.");
        return ApiResponse.createFail("입력이 올바르지 못합니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> missingRequestHeaderExceptionHandler(MissingRequestHeaderException e) {
        String exceptionMessage = e.getMessage();
        log.error("[MissingRequestHeaderExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail("권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> noAuthorizationExceptionHandler(NoAuthorizationException e) {
        String exceptionMessage = e.getMessage();
        log.error("[NoAuthorizationExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> userNotFoundExceptionHandler(UserNotFoundException e) {
        String exceptionMessage = e.getMessage();
        log.error("[UserNotFoundExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ApiResponse<String> duplicateNicknameExceptionHandler(DuplicateNicknameException e) {
        String exceptionMessage = e.getMessage();
        log.error("[DuplicateNicknameExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ApiResponse<String> badCredentialsExceptionHandler(BadCredentialsException e) {
        String exceptionMessage = e.getMessage();
        log.error("[BadCredentialsExceptionHandler] ex : " + exceptionMessage);
        return ApiResponse.createFail(exceptionMessage);
    }
}

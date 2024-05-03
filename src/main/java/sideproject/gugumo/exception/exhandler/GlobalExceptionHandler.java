package sideproject.gugumo.exception.exhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sideproject.gugumo.exception.exception.NoSuchBookmarkException;
import sideproject.gugumo.exception.exception.NoSuchMeetingException;
import sideproject.gugumo.exception.exception.NoSuchPostException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NoSuchPostException.class})
    public ResponseEntity noSuchPostExceptionHandler(NoSuchPostException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[noSuchPostExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }

    @ExceptionHandler(value = {NoSuchMeetingException.class})
    public ResponseEntity noSuchMeetingExceptionHandler(NoSuchMeetingException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[noSuchMeetingExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }


    @ExceptionHandler(value = {NoSuchBookmarkException.class})
    public ResponseEntity noSuchBookmarkExceptionHandler(NoSuchBookmarkException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[noSuchBookmarkExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }
}

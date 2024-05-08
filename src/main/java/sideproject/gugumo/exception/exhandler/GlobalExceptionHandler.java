package sideproject.gugumo.exception.exhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sideproject.gugumo.exception.NoAuthorizationException;
import sideproject.gugumo.exception.exception.BookmarkNotFoundException;
import sideproject.gugumo.exception.exception.DuplicateBookmarkException;
import sideproject.gugumo.exception.exception.MeetingNotFoundException;
import sideproject.gugumo.exception.exception.PostNotFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {PostNotFoundException.class})
    public ResponseEntity postNotFoundExceptionHandler(PostNotFoundException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[postNotFoundExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }

    @ExceptionHandler(value = {MeetingNotFoundException.class})
    public ResponseEntity meetingNotFoundExceptionHandler(MeetingNotFoundException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[meetingNotFoundExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }


    @ExceptionHandler(value = {BookmarkNotFoundException.class})
    public ResponseEntity bookmarkNotFoundExceptionHandler(BookmarkNotFoundException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[bookmarkNotFoundExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(404).body(result);
    }

    @ExceptionHandler(value = {NoAuthorizationException.class})
    public ResponseEntity noAuthorizationExceptionHandler(NoAuthorizationException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[noAuthorizationExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(401).body(result);
    }

    @ExceptionHandler(value = {DuplicateBookmarkException.class})
    public ResponseEntity duplicateBookmarkExceptionHandler(DuplicateBookmarkException e) {
        Map<String, String> result = new HashMap<>();
        result.put("ErrorMessage", e.getMessage());
        log.error("[duplicateBookmarkExceptionHandler] ex : " + e.getMessage());
        return ResponseEntity.status(400).body(result);
    }
}

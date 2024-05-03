package sideproject.gugumo.exception.exception;

public class NoSuchBookmarkException extends RuntimeException{

    public NoSuchBookmarkException() {
    }

    public NoSuchBookmarkException(String message) {
        super(message);
    }

    public NoSuchBookmarkException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchBookmarkException(Throwable cause) {
        super(cause);
    }
}

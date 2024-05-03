package sideproject.gugumo.exception.exception;

public class NoSuchPostException extends RuntimeException{

    public NoSuchPostException() {
    }

    public NoSuchPostException(String message) {
        super(message);
    }

    public NoSuchPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchPostException(Throwable cause) {
        super(cause);
    }


}

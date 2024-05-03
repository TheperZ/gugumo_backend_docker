package sideproject.gugumo.exception.exception;

public class meetingNotFoundException extends RuntimeException{

    public meetingNotFoundException() {
    }

    public meetingNotFoundException(String message) {
        super(message);
    }

    public meetingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public meetingNotFoundException(Throwable cause) {
        super(cause);
    }
}

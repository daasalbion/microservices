package py.com.daas.microservice.commons.exceptions;

public class AppException extends RuntimeException {

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

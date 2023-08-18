package py.com.daas.microservice.commons.models;

import java.time.LocalDateTime;

public class AppErrorResponse {

    private final int status;
    private final int code;
    private final String message;
    private final LocalDateTime dateTime;

    public AppErrorResponse(int status, int code, String message, LocalDateTime dateTime) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.dateTime = dateTime;
    }

    public int getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

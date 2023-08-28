package py.com.daas.microservice.commons.models;

import java.time.LocalDateTime;

public record AppErrorResponse(int status, int code, String message, LocalDateTime dateTime) {
}

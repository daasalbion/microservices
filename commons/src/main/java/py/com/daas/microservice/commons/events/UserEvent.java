package py.com.daas.microservice.commons.events;

public record UserEvent(String displayName, String username, String password) {
}

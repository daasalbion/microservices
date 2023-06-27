package py.com.daas.microservices.commons.events;

public record UserEvent(String displayName, String username, String password) {
}

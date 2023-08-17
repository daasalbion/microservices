package py.com.daas.microservice.auth.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import py.com.daas.microservice.auth.services.UserService;
import py.com.daas.microservice.commons.events.UserEvent;

@Component
public class EventConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventConsumer.class);

    private final UserService userService;

    public EventConsumer(UserService userService) {
        this.userService = userService;
    }

    @KafkaListener(topics = "${kafka.topic}", groupId = "users", autoStartup = "${kafka.enabled}")
    public void listenGroupUsers(UserEvent userEvent) {
        LOGGER.info("Received Message in group users, event = {}", userEvent);
        userService.create(userEvent);
    }
}

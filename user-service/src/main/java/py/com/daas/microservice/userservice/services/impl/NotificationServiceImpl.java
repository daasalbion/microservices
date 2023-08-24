package py.com.daas.microservice.userservice.services.impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import py.com.daas.microservice.commons.events.UserEvent;
import py.com.daas.microservice.userservice.entities.User;
import py.com.daas.microservice.userservice.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final String topic;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public NotificationServiceImpl(@Value("${kafka.topic}") String topic,
            KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    @Override
    public void sendNewUser(User user) {
        UserEvent msg = new UserEvent(user.getFullName(), user.getUsername(), user.getPassword());
        try {
            kafkaTemplate.send(topic, msg).get(1, TimeUnit.SECONDS);
            LOGGER.info("message = {} sent to users topic = {}", msg, topic);
        } catch (Exception ex) {
            LOGGER.error("message = {} can't be send to topic = {}", msg, topic, ex);
        }
    }
}

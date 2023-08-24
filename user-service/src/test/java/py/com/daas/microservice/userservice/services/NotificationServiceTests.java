package py.com.daas.microservice.userservice.services;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import py.com.daas.microservice.userservice.services.impl.NotificationServiceImpl;

class NotificationServiceTests {

    private final KafkaTemplate kafkaTemplate = Mockito.mock(KafkaTemplate.class);
    private NotificationService notificationService;

    @BeforeEach
    public void setup() {
        notificationService = new NotificationServiceImpl("users", kafkaTemplate);
    }

    @Test
    void sendNewUserTestOk() {
        Mockito.when(kafkaTemplate.send(any(), any())).thenReturn(any());
    }

}

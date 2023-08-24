package py.com.daas.microservice.userservice.configurations;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

@EnableKafka
@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${kafka.topic}")
    private String topic;

    @Value("${kafka.enabled}")
    private boolean isEnabled;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        if (isEnabled) {
            configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        }
        configs.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "30");
        configs.put(AdminClientConfig.RETRIES_CONFIG, "30");

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic(topic, 1, (short) 1);
    }
}

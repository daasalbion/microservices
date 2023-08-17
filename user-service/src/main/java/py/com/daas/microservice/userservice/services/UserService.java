package py.com.daas.microservice.userservice.services;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.NotFoundException;
import py.com.daas.microservice.userservice.entities.User;
import py.com.daas.microservice.userservice.repositories.UserRepository;
import py.com.daas.microservice.commons.events.UserEvent;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserEvent> kafkaTemplate;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, KafkaTemplate<String, UserEvent> kafkaTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        sendMessage(new UserEvent(newUser.getDisplayName(), newUser.getUsername(), newUser.getPassword()));
        return newUser;
    }

    public User get(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public void delete(Long id) {
        User user = get(id);
        userRepository.delete(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    private void sendMessage(UserEvent msg) {
        kafkaTemplate.send("users", msg);
    }
}

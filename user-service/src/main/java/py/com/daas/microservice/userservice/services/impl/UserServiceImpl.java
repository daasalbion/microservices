package py.com.daas.microservice.userservice.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import py.com.daas.microservice.commons.exceptions.AppException;
import py.com.daas.microservice.userservice.dtos.UserDto;
import py.com.daas.microservice.userservice.entities.User;
import py.com.daas.microservice.userservice.repositories.UserRepository;
import py.com.daas.microservice.userservice.services.NotificationService;
import py.com.daas.microservice.userservice.services.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public UserServiceImpl(
        PasswordEncoder passwordEncoder,
        UserRepository userRepository,
        NotificationService notificationService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public UserDto create(UserDto userDto) {
        // checks
        boolean existsUsername = userRepository.existsByUsername(userDto.email());
        if (existsUsername) {
            throw new AppException(String.format(USERNAME_EXISTS, userDto.email()));
        }
        User user = UserService.toUser(userDto, passwordEncoder);
        User newUser = userRepository.save(user);
        sendMessage(newUser);

        return UserService.toUserDto(newUser);
    }

    @Override
    public UserDto get(Long id) {
        User user = getRequiredUser(id);
        return UserService.toUserDto(user);
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        User savedUser = getRequiredUser(id);
        // checks
        boolean hasUsernameChanged = !savedUser.getUsername().equals(userDto.email());
        boolean hasPasswordChanged = !savedUser.getPassword().equals(userDto.password());
        if (hasUsernameChanged && userRepository.existsByUsername(userDto.email())) {
            throw new AppException(String.format(USERNAME_EXISTS, userDto.email()));
        }
        User updatedUser;
        if (hasPasswordChanged) {
            updatedUser = UserService.toUser(userDto, passwordEncoder);
        } else {
            updatedUser = UserService.toUser(userDto);
        }

        return UserService.toUserDto(userRepository.save(updatedUser));
    }

    @Override
    public UserDto delete(Long id) {
        User user = getRequiredUser(id);
        userRepository.delete(user);
        return UserService.toUserDto(user);
    }

    @Override
    public Page<UserDto> list(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        List<UserDto> list = page.stream()
                .map(UserService::toUserDto).toList();
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }

    private User getRequiredUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException("User not found"));
    }

    // ref: https://docs.spring.io/spring-kafka/reference/html/#kafka-template
    private void sendMessage(User newUser) {
        LOGGER.info("sending User = {} to external service", newUser);
        notificationService.sendNewUser(newUser);
    }
}

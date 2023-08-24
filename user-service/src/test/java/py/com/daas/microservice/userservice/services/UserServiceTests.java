package py.com.daas.microservice.userservice.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static py.com.daas.microservice.userservice.services.UserService.USERNAME_EXISTS;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import py.com.daas.microservice.commons.exceptions.AppException;
import py.com.daas.microservice.userservice.dtos.UserDto;
import py.com.daas.microservice.userservice.entities.User;
import py.com.daas.microservice.userservice.repositories.UserRepository;
import py.com.daas.microservice.userservice.services.impl.UserServiceImpl;

class UserServiceTests {

    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final NotificationService notificationService = Mockito.mock(NotificationService.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private UserService userService;
    private User user;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(passwordEncoder, userRepository, notificationService);
        user = new User(1L, "daas", "daas", "daas");
    }

    @Test
    void saveTestOk() {
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(any())).thenReturn(user);

        assertNotNull(userService.create(UserService.toUserDto(user)));
    }

    @Test
    void saveTestExistUsernameError() {
        UserDto userDto = UserService.toUserDto(user);
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> userService.create(userDto));
        Assertions.assertThat(ex.getMessage()).contains(String.format(USERNAME_EXISTS, userDto.email()));
    }

    @Test
    void getTestOk() {
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertNotNull(userService.get(user.getId()));
    }

    @Test
    void getTestUserNotFound() {
        Long userId = user.getId();
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> userService.get(userId));
        Assertions.assertThat(ex.getMessage()).contains("User not found");
    }

    @Test
    void updateTestOk() {
        UserDto userDto = UserService.toUserDto(user);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);

        assertNotNull(userService.update(user.getId(), userDto));
    }

    @Test
    void updateTestUserNotFound() {
        Long userId = user.getId();
        UserDto userDto = UserService.toUserDto(user);
        Mockito.when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> userService.update(userId, userDto));
        Assertions.assertThat(ex.getMessage()).contains("User not found");
    }

    @Test
    void updateTestUsernameAlreadyExists() {
        Long userId = user.getId();
        User modifiedUser = new User(1L, "daas", "daas2", "daas");
        UserDto userDto = UserService.toUserDto(modifiedUser);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.existsByUsername(userDto.email())).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> userService.update(userId, userDto));
        Assertions.assertThat(ex.getMessage()).contains(String.format(USERNAME_EXISTS, userDto.email()));
    }

    @Test
    void deleteTestOk() {
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertNotNull(userService.delete(user.getId()));
    }

    @Test
    void deleteTestUserNotFound() {
        Long userId = user.getId();
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> userService.delete(userId));
        Assertions.assertThat(ex.getMessage()).contains("User not found");
    }

    @Test
    void listTestOk() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);

        assertNotNull(userService.list(pageable));
    }
}

package py.com.daas.microservice.auth.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import py.com.daas.microservice.auth.entities.Role;
import py.com.daas.microservice.auth.entities.User;
import py.com.daas.microservice.auth.repositories.RoleRepository;
import py.com.daas.microservice.auth.repositories.UserRepository;
import py.com.daas.microservice.auth.services.impl.UserServiceImpl;

class UserServiceTests {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final RoleRepository roleRepository = Mockito.mock(RoleRepository.class);
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserServiceImpl(userRepository, roleRepository);
    }

    @Test
    void loadUserByUsernameTestOk() {
        Role role = new Role("rol", "rol");
        User user = new User("daas", "daas", "daas", Collections.singletonList(role));
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        assertNotNull(userService.loadUserByUsername(user.getUsername()));
    }

    @Test
    void loadUserByUsernameTestNotFound() {
        String username = "daas";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(username));
        Assertions.assertThat(ex.getMessage()).contains("does not exist");
    }
}

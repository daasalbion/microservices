package py.com.daas.microservice.auth.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static py.com.daas.microservice.auth.services.UserService.LOGIN_FAILED;

import java.util.Collections;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import py.com.daas.microservice.auth.dtos.LoginDto;
import py.com.daas.microservice.auth.entities.User;
import py.com.daas.microservice.auth.services.impl.AuthServiceImpl;
import py.com.daas.microservice.commons.exceptions.AppException;

class AuthServiceTests {

    private final AuthenticationManager authenticationManager = Mockito.mock(AuthenticationManager.class);
    private final UserService userService = Mockito.mock(UserService.class);
    private final JwtService jwtService = Mockito.mock(JwtService.class);
    private AuthService authService;

    @BeforeEach
    public void setup() {
        authService = new AuthServiceImpl(authenticationManager, jwtService, userService);
    }

    @Test
    void loginTestOk() {
        User user = new User("daas", "daas", "daas", Collections.emptyList());
        UsernamePasswordAuthenticationToken usernamePasswordToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(usernamePasswordToken)).thenReturn(usernamePasswordToken);
        Mockito.when(jwtService.generateToken(user.getUsername(), Collections.emptyList()))
                .thenReturn(Optional.of("token"));

        assertNotNull(authService.login(new LoginDto(user.getUsername(), user.getPassword())));
    }

    @Test
    void loginTestUserNotFound() {
        String username = "daas";
        Mockito.when(userService.getByUsername(username)).thenReturn(Optional.empty());
        AppException ex = assertThrows(AppException.class, () -> authService.login(new LoginDto(username, "password")));
        Assertions.assertThat(ex.getMessage()).contains(String.format(LOGIN_FAILED, username));
    }

    @Test
    void loginTestUserBadCredentials() {
        User user = new User("daas", "daas", "daas", Collections.emptyList());
        Mockito.when(userService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenThrow(new BadCredentialsException("Bad " +
                "credentials"));
        AppException ex = assertThrows(AppException.class, () -> authService.login(new LoginDto(user.getUsername(),
                user.getPassword())));
        Assertions.assertThat(ex.getMessage()).contains(String.format(LOGIN_FAILED, user.getUsername()));
    }
}

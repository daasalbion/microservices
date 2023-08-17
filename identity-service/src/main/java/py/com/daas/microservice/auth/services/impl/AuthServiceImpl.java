package py.com.daas.microservice.auth.services.impl;

import static py.com.daas.microservice.auth.services.UserService.LOGIN_FAILED;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import py.com.daas.microservice.auth.dtos.LoginDto;
import py.com.daas.microservice.auth.entities.User;
import py.com.daas.microservice.auth.services.AuthService;
import py.com.daas.microservice.auth.services.JwtService;
import py.com.daas.microservice.auth.services.UserService;
import py.com.daas.microservice.commons.exceptions.AppException;

@Service
public class AuthServiceImpl implements AuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthServiceImpl(
        AuthenticationManager authenticationManager,
        JwtService jwtService,
        UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public String login(LoginDto loginDto) {
        LOGGER.info("User = {} attempting to log in", loginDto.username());
        return getAuthenticationToken(loginDto)
                .flatMap(this::authenticate)
                .flatMap(this::generateToken)
                .orElseThrow(() -> new AppException(String.format(LOGIN_FAILED, loginDto.username())));
    }

    @Override
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private Optional<UsernamePasswordAuthenticationToken> getAuthenticationToken(LoginDto loginDto) {
        Optional<UsernamePasswordAuthenticationToken> token = userService.getByUsername(loginDto.username())
                        .map(User::getUsername)
                        .map(username -> new UsernamePasswordAuthenticationToken(username, loginDto.password()));
        if (token.isEmpty()) {
            LOGGER.info("User = {} not found", loginDto.username());
        }

        return token;
    }

    private Optional<Authentication> authenticate(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            return Optional.of(authenticationManager.authenticate(authenticationToken));
        } catch (AuthenticationException ex) {
            LOGGER.info("User = {} authentication failed", authenticationToken.getName(), ex);
            return Optional.empty();
        }
    }

    private Optional<String> generateToken(Authentication authentication) {
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return jwtService.generateToken(authentication.getName(), authorities);
    }

}

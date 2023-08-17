package py.com.daas.microservice.gateway.security;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import py.com.daas.microservice.gateway.clients.AuthServiceClient;
import py.com.daas.microservice.gateway.services.UserService;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final AuthServiceClient authServiceClient;
    private final UserService userDetailsService;

    public AuthenticationManager(@Lazy AuthServiceClient authServiceClient, UserService userDetailsService) {
        this.authServiceClient = authServiceClient;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        if (authToken == null) {
            return Mono.empty();
        }

        return Mono.just(isValidToken(authToken))
                .filter(valid -> valid)
                .switchIfEmpty(Mono.empty())
                .map(valid -> userDetailsService.loadUserByJwtToken(authToken)
                        .map(userDetails -> new UsernamePasswordAuthenticationToken(
                                        userDetails.getUsername(),
                                        null,
                                        userDetails.getAuthorities()
                                )
                        ).orElseThrow(() -> new IllegalArgumentException("Access Denied"))
                );
    }

    private boolean isValidToken(String token) {
        return authServiceClient.validateToken(token);
    }
}
package py.com.daas.microservice.gateway.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;

import py.com.daas.microservice.gateway.clients.AuthServiceClient;
import py.com.daas.microservice.gateway.services.JwtService;
import py.com.daas.microservice.gateway.services.UserDetailsServiceImpl;
import py.com.daas.microservice.gateway.services.UserService;

/**
 * this was based on this <a href="https://github.com/ard333/spring-boot-webflux-jjwt">repo</a>
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    private final JwtService jwtProvider;
    private final AuthServiceClient authServiceClient;

    public SecurityConfig(JwtService jwtProvider, AuthServiceClient authServiceClient) {
        this.jwtProvider = jwtProvider;
        this.authServiceClient = authServiceClient;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(CsrfSpec::disable)
                .authenticationManager(authenticationManager())
                .securityContextRepository(securityContextRepository())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/auth/login", "/api/auth/validate", "/actuator/**").permitAll()
                        .pathMatchers("/api/users/**").hasAnyRole("USER", "ADMIN")
                        .anyExchange().authenticated()
                )
                .httpBasic(withDefaults())
                .build();
    }

    @Bean
    public UserService userDetailsService() {
        return new UserDetailsServiceImpl(jwtProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new AuthenticationManager(authServiceClient, userDetailsService());
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new SecurityContextRepository(authenticationManager());
    }

}

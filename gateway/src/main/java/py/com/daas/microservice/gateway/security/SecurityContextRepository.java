package py.com.daas.microservice.gateway.security;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextRepository.class);

    private static final String BEARER = "Bearer";
    private static final Set<String> PUBLIC_URLS = Set.of("/api/auth/login", "/api/auth/validate");

    private final AuthenticationManager authenticationManager;

    public SecurityContextRepository(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
        throw new UnsupportedOperationException("Saving SecurityContext is not supported.");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange swe) {
        String uri = swe.getRequest().getURI().getPath();
        LOGGER.info("Processing request for URI = {}", uri);

        if (PUBLIC_URLS.contains(uri)) {
            return Mono.empty();
        }

        return Mono
                .justOrEmpty(swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER))
                .flatMap(authHeader -> {
                    String authToken = authHeader.substring(BEARER.length()).trim();
                    Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                    return authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                });
    }
}
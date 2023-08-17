package py.com.daas.microservice.auth.services;

import java.util.List;
import java.util.Optional;

public interface JwtService {

    String ROLES_KEY = "roles";

    Optional<String> generateToken(String username, List<String> authorities);
    boolean validateToken(String token);
}

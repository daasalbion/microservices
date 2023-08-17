package py.com.daas.microservice.gateway.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    Optional<UserDetails> loadUserByJwtToken(String token) throws UsernameNotFoundException;
}

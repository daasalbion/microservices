package py.com.daas.microservice.gateway.services;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserService {

    private final JwtService jwtProvider;

    public UserDetailsServiceImpl(JwtService jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public Optional<UserDetails> loadUserByJwtToken(String jwtToken) throws UsernameNotFoundException {
            return Optional.of(
                withUsername(jwtProvider.getUsername(jwtToken))
                .authorities(jwtProvider.getRoles(jwtToken))
                .password("") //token does not have password but field may not be empty
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build()
            );
    }

}

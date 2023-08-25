package py.com.daas.microservice.auth.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import py.com.daas.microservice.auth.entities.User;
import py.com.daas.microservice.commons.events.UserEvent;

public interface UserService extends UserDetailsService {

    String LOGIN_FAILED = "User = %s, login failed";

    void create(UserEvent userEvent);
    Optional<User> getByUsername(String username);
    Page<User> list(Pageable pageable);

}

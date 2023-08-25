package py.com.daas.microservice.auth.services.impl;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import py.com.daas.microservice.auth.entities.Role;
import py.com.daas.microservice.auth.entities.User;
import py.com.daas.microservice.auth.repositories.RoleRepository;
import py.com.daas.microservice.auth.repositories.UserRepository;
import py.com.daas.microservice.auth.services.UserService;
import py.com.daas.microservice.commons.events.UserEvent;
import py.com.daas.microservice.commons.exceptions.AppException;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional //ref: https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with " +
                        "name %s does not exist", username)));
        List<SimpleGrantedAuthority> userAuthorities = user.getRoles()
                .stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .toList();

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(userAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    @Override
    public void create(UserEvent userEvent) {
        Optional<User> user = getByUsername(userEvent.username());
        if (user.isPresent()) {
            LOGGER.info("User = {} already created", user);
            return;
        }

        try {
            Role baseRole = roleRepository.findRolByName("ROLE_USER")
                    .orElseThrow(() -> new AppException("Rol ROLE_USER is not present"));
            User newUser = userRepository.save(new User(userEvent.displayName(), userEvent.username(),
                    userEvent.password(), Collections.singletonList(baseRole)));
            LOGGER.info("User = {} created", newUser);
        } catch (Exception ex) {
            LOGGER.error("Failed to create user = {}", userEvent.username(), ex);
        }
    }

    @Override
    public Page<User> list(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

}

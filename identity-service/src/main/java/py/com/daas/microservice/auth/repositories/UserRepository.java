package py.com.daas.microservice.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.daas.microservice.auth.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

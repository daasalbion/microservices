package py.com.daas.microservice.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import py.com.daas.microservice.auth.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRolByName(String name);
}

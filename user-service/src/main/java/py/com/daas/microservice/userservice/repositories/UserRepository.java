package py.com.daas.microservice.userservice.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import py.com.daas.microservice.userservice.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
}

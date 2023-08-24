package py.com.daas.microservice.userservice.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import py.com.daas.microservice.userservice.dtos.UserDto;
import py.com.daas.microservice.userservice.entities.User;

public interface UserService {
    String USERNAME_EXISTS = "User with username = %s, already exists";

    UserDto create(UserDto user);
    UserDto get(Long id);
    UserDto update(Long id, UserDto user);
    UserDto delete(Long id);
    Page<UserDto> list(Pageable pageable);

    /**
     * For creating a User
     * @param userDto dto for user
     * @return User entity
     */
    static User toUser(UserDto userDto) {
        return new User(userDto.id(), userDto.fullName(), userDto.email(), userDto.password());
    }

    static User toUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        return new User(userDto.id(), userDto.fullName(), userDto.email(), passwordEncoder.encode(userDto.password()));
    }

    static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getFullName(), user.getUsername(), user.getPassword());
    }
}

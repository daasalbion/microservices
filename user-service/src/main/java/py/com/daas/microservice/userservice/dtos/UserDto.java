package py.com.daas.microservice.userservice.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserDto(
        Long id,
        @NotEmpty(message = "fullName cannot be null") String fullName,
        @Email @NotEmpty(message = "email cannot be null") String email,
        @NotEmpty(message = "password cannot be null") String password) {
}

package py.com.daas.microservice.auth.dtos;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty(message = "username cannot be null") String username,
        @NotEmpty(message = "password cannot be null") String password) {
}

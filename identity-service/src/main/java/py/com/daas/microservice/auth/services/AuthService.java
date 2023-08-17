package py.com.daas.microservice.auth.services;

import py.com.daas.microservice.auth.dtos.LoginDto;

public interface AuthService {

    String login(LoginDto loginDto);
    boolean validateToken(String token);

}

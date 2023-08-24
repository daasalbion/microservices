package py.com.daas.microservice.userservice.services;

import py.com.daas.microservice.userservice.entities.User;

public interface NotificationService {

    void sendNewUser(User newUser);
}

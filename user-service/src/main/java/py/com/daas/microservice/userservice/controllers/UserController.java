package py.com.daas.microservice.userservice.controllers;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import py.com.daas.microservice.commons.exceptions.AppException;
import py.com.daas.microservice.commons.models.AppErrorResponse;
import py.com.daas.microservice.userservice.dtos.UserDto;
import py.com.daas.microservice.userservice.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@RequestBody @Valid UserDto user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable("id") Long id, @RequestBody @Valid UserDto user) {
        return userService.update(id, user);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    public UserDto delete(@PathVariable("id") Long id) {
        return userService.delete(id);
    }

    @GetMapping
    public Page<UserDto> getUsers(@RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") Integer size) {
        return userService.list(PageRequest.of(page, size));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private AppErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String message = exception.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new AppErrorResponse(exception.getStatusCode().value(), exception.getStatusCode().value(), message, LocalDateTime.now());
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private AppErrorResponse handleAppException(AppException exception) {
        return new AppErrorResponse(HttpStatus.BAD_REQUEST.value(), 300, exception.getMessage(), LocalDateTime.now());
    }
}

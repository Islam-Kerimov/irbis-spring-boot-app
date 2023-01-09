package ru.plus.irbis.web.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.plus.irbis.web.app.model.dto.UserDto;
import ru.plus.irbis.web.app.model.entity.User;
import ru.plus.irbis.web.app.model.mapper.UserMapper;
import ru.plus.irbis.web.app.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@Tag(name = "User", description = "The User API. Contains all the operations that can be performed on a user.")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        return userMapper.entityUserListToDtoList(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id")
    public UserDto getUserById(@PathVariable @Min(1) Integer id) {
        Optional<User> user = userService.findById(id);
        return userMapper.entityUserToDto(user
                .orElseThrow(() -> new NoSuchElementException("There is no user with ID = " + id + " in Database")));
    }
}

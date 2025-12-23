package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequestDTO;
import ru.practicum.shareit.user.dto.UpdateUserRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserDTO saveNewUser(@RequestBody @Valid NewUserRequestDTO userRequestDTO) {
        return userService.saveUser(userRequestDTO);
    }

    @PatchMapping("/{userId}")
    public UserDTO updateUser(@PathVariable("userId") long userId, @RequestBody UpdateUserRequestDTO request) {
        return userService.updateUser(userId, request);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUserById(@PathVariable("userId") long userId) {
        return userService.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUser(@PathVariable("userId") long userId) {
        userService.deleteUser(userId);
    }

}
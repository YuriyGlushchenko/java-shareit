package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
//    private final UserService userService;
//
//    @GetMapping
//    public List<UserDto> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserDto saveNewUser(@RequestBody @Valid NewUserRequestDto userRequestDTO) {
//        return userService.saveUser(userRequestDTO);
//    }
//
//    @PatchMapping("/{userId}")
//    public UserDto updateUser(@PathVariable("userId") long userId, @RequestBody UpdateUserRequestDto request) {
//        return userService.updateUser(userId, request);
//    }
//
//    @GetMapping("/{userId}")
//    public UserDto getUserById(@PathVariable("userId") long userId) {
//        return userService.getUserById(userId);
//    }
//
//    @DeleteMapping("/{userId}")
//    @ResponseStatus(HttpStatus.OK)
//    public void deleteUser(@PathVariable("userId") long userId) {
//        userService.deleteUser(userId);
//    }

}
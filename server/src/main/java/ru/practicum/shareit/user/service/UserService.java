package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto saveUser(NewUserRequestDto user);

    UserDto updateUser(long userId, UpdateUserRequestDto request);

    UserDto getUserById(long id);

    void deleteUser(long id);
}
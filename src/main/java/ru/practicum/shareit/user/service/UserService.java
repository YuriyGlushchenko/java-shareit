package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.NewUserRequestDTO;
import ru.practicum.shareit.user.dto.UpdateUserRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    UserDTO saveUser(NewUserRequestDTO user);

    UserDTO updateUser(long userId, UpdateUserRequestDTO request);

    UserDTO getUserById(long id);

    void deleteUser(long id);
}
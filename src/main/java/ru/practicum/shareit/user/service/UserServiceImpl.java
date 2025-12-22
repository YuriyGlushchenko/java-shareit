package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.DuplicatedDataException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequestDTO;
import ru.practicum.shareit.user.dto.UpdateUserRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserDTO saveUser(NewUserRequestDTO request) {
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new ConditionsNotMetException("email должен быть указан");
        }

        Optional<User> existedUser = userRepository.findByEmail(request.getEmail());
        if (existedUser.isPresent()) {
            throw new DuplicatedDataException("Данный email уже используется");
        }


        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDTO updateUser(long userId, UpdateUserRequestDTO request) {
        Optional<User> userWithExistedEmail = userRepository.findByEmail(request.getEmail());

        if (userWithExistedEmail.isPresent() && userWithExistedEmail.get().getId() != userId) {
            throw new DuplicatedDataException("Данный email уже используется");
        }

        User updatedUser = userRepository.findById(userId)
                .map(user -> UserMapper.updateUserFields(user, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        updatedUser = userRepository.update(updatedUser);

        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDTO getUserById(long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь c id: " + id + " не найден"));
    }

    public void deleteUser(long id) {
        userRepository.delete(id);
    }
}
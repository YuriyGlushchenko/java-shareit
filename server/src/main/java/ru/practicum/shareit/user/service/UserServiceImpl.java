package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.DuplicatedDataException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).toList();
    }

    @Override
    public UserDto saveUser(NewUserRequestDto request) {

        User user = UserMapper.mapToUser(request);
        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto updateUser(long userId, UpdateUserRequestDto request) {
        Optional<User> userWithExistedEmail = userRepository.findByEmail(request.getEmail());

        if (userWithExistedEmail.isPresent() && userWithExistedEmail.get().getId() != userId) {
            throw new DuplicatedDataException("Данный email уже используется");
        }

        User user = userRepository.findById(userId)
                .map(u -> UserMapper.updateUserFields(u, request))
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        user = userRepository.save(user);

        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto getUserById(long id) {
        return userRepository.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь c id: " + id + " не найден"));
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
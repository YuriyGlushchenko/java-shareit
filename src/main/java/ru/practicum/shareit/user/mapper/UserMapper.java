package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.NewUserRequestDTO;
import ru.practicum.shareit.user.dto.UpdateUserRequestDTO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(NewUserRequestDTO request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static UserDTO mapToUserDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequestDTO request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasNname()) {
            user.setName(request.getName());
        }
        return user;
    }
}
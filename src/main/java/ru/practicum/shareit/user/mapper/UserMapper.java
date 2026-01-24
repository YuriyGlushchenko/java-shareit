package ru.practicum.shareit.user.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    public static User mapToUser(NewUserRequestDto request) {
        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User updateUserFields(User user, UpdateUserRequestDto request) {
        if (request.hasEmail()) {
            user.setEmail(request.getEmail());
        }
        if (request.hasNname()) {
            user.setName(request.getName());
        }
        return user;
    }
}
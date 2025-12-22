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
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setPassword(request.getPassword());
//        user.setEmail(request.getEmail());
//        user.setRegistrationDate(Instant.now());
//
//        return user;

        return User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .build();
    }

    public static UserDTO mapToUserDto(User user) {
//        UserDto dto = new UserDto();
//        dto.setId(user.getId());
//        dto.setUsername(user.getUsername());
//        dto.setEmail(user.getEmail());
//        dto.setRegistrationDate(Instant.now());
//        return dto;

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getEmail())
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
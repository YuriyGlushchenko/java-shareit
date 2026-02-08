package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.exceptions.DuplicatedDataException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllUsers_success() {
        User user1 = User.builder().id(1L).name("Ivan").email("ivan@mail.ru").build();
        User user2 = User.builder().id(2L).name("Anna").email("anna@mail.ru").build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.getAllUsers();

        assertThat(result.size(), equalTo(2));
        assertThat(result.getFirst().getName(), equalTo("Ivan"));
        assertThat(result.getLast().getName(), equalTo("Anna"));
        verify(userRepository).findAll();
    }

    @Test
    void saveUser_success() {
        NewUserRequestDto request = new NewUserRequestDto();
        request.setName("Ivan");
        request.setEmail("ivan@mail.ru");

        User savedUser = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.saveUser(request);

        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getEmail(), equalTo("ivan@mail.ru"));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserById_success() {
        User user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(1L);

        assertThat(result, hasProperty("id", equalTo(1L)));
        assertThat(result, hasProperty("name", equalTo("Ivan")));
        assertThat(result, hasProperty("email", equalTo("ivan@mail.ru")));
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getUserById(1L));
    }

    @Test
    void updateUser_success_updateEmailAndName() {
        User existingUser = User.builder()
                .id(1L)
                .name("Ivan")
                .email("old@mail.ru")
                .build();

        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setName("NewName");
        request.setEmail("new@mail.ru");

        when(userRepository.findByEmail("new@mail.ru")).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserDto result = userService.updateUser(1L, request);

        assertThat(result.getName(), equalTo("NewName"));
        assertThat(result.getEmail(), equalTo("new@mail.ru"));
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUser_duplicateEmail_throwsException() {
        User anotherUser = User.builder()
                .id(2L)
                .email("test@mail.ru")
                .name("Anna")
                .build();

        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setEmail("test@mail.ru");

        when(userRepository.findByEmail("test@mail.ru"))
                .thenReturn(Optional.of(anotherUser));

        assertThrows(DuplicatedDataException.class,
                () -> userService.updateUser(1L, request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_notFound() {
        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setName("NewName");

        when(userRepository.findByEmail(null)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.updateUser(1L, request));
    }

    @Test
    void deleteUser_success() {
        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }
}

package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void getAllUsers_success() throws Exception {
        UserDto user1 = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        UserDto user2 = UserDto.builder()
                .id(2L)
                .name("Anna")
                .email("anna@mail.ru")
                .build();

        Mockito.when(userService.getAllUsers())
                .thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Ivan"))
                .andExpect(jsonPath("$[1].name").value("Anna"));
    }

    @Test
    void saveNewUser_success() throws Exception {
        NewUserRequestDto request = new NewUserRequestDto();
        request.setName("Ivan");
        request.setEmail("ivan@mail.ru");

        UserDto response = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        Mockito.when(userService.saveUser(any(NewUserRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("ivan@mail.ru"));
    }

    @Test
    void updateUser_success() throws Exception {
        UpdateUserRequestDto request = new UpdateUserRequestDto();
        request.setName("NewName");
        request.setEmail("new@mail.ru");

        UserDto response = UserDto.builder()
                .id(1L)
                .name("NewName")
                .email("new@mail.ru")
                .build();

        Mockito.when(userService.updateUser(eq(1L), any(UpdateUserRequestDto.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("NewName"))
                .andExpect(jsonPath("$.email").value("new@mail.ru"));
    }

    @Test
    void getUserById_success() throws Exception {
        UserDto response = UserDto.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        Mockito.when(userService.getUserById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Ivan"));
    }

    @Test
    void deleteUser_success() throws Exception {
        mockMvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());

        Mockito.verify(userService).deleteUser(1L);
    }
}

package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.NewUserRequestDto;
import ru.practicum.shareit.user.dto.UpdateUserRequestDto;

import java.util.Map;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }

    public ResponseEntity<Object> saveUser(NewUserRequestDto userRequestDTO) {
        return post("", userRequestDTO);
    }

    public ResponseEntity<Object> updateUser(Long userId, UpdateUserRequestDto requestDto) {
        return patch("/" + userId, userId, requestDto);
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        return get("/" + userId, userId);
    }

    public void deleteUser(Long userId) {
        delete("/" + userId, userId);
    }


    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getOwnerItemsBookings(long userId, State state) {
        Map<String, Object> parameters = Map.of(
                "state", state.getValue()
        );
        return get("/owner?state={state}", userId, parameters);
    }
}

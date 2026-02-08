package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.NewRequestDtoDetailed;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemRequestServiceImpl requestService;

    @Test
    void addNewRequest_success() {
        User user = User.builder()
                .id(1L)
                .name("Ivan")
                .email("ivan@mail.ru")
                .build();

        NewRequestDtoDetailed dto = NewRequestDtoDetailed.builder()
                .description("Need a drill")
                .build();

        ItemRequest savedRequest = ItemRequest.builder()
                .id(10L)
                .description("Need a drill")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.save(any(ItemRequest.class)))
                .thenReturn(savedRequest);

        RequestDto result = requestService.addNewRequest(1L, dto);

        assertThat(result.getId(), equalTo(10L));
        assertThat(result.getDescription(), equalTo("Need a drill"));
        assertThat(result.getRequestor().getId(), equalTo(1L));

        verify(itemRequestRepository).save(any(ItemRequest.class));
    }

    @Test
    void addNewRequest_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NewRequestDtoDetailed dto = NewRequestDtoDetailed.builder()
                .description("Need a drill")
                .build();

        assertThrows(NotFoundException.class,
                () -> requestService.addNewRequest(1L, dto));
    }

    @Test
    void getUserRequests_success() {
        User user = User.builder()
                .id(1L)
                .name("Ivan")
                .build();

        Item item = Item.builder()
                .id(100L)
                .name("Drill")
                .owner(user)
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(10L)
                .description("Need a drill")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        request.addItem(item);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdWithItems(1L))
                .thenReturn(List.of(request));

        List<RequestDto> result = requestService.getUserRequests(1L);

        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getId(), equalTo(10L));
        assertThat(result.getFirst().getItems(), hasSize(1));
        assertThat(result.getFirst().getItems().getFirst().getId(), equalTo(100L));
    }

    @Test
    void getUserRequests_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getUserRequests(1L));
    }

    @Test
    void getOtherUsersRequests_success() {
        User user = User.builder()
                .id(1L)
                .build();

        User otherUser = User.builder()
                .id(2L)
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(20L)
                .description("Need ladder")
                .requestor(otherUser)
                .created(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(1L))
                .thenReturn(List.of(request));

        List<RequestDto> result = requestService.getOtherUsersRequests(1L);

        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getId(), equalTo(20L));
        assertThat(result.getFirst().getDescription(), equalTo("Need ladder"));
    }

    @Test
    void getOtherUsersRequests_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getOtherUsersRequests(1L));
    }

    @Test
    void getRequest_success() {
        User user = User.builder()
                .id(1L)
                .name("Ivan")
                .build();

        Item item = Item.builder()
                .id(100L)
                .name("Drill")
                .owner(user)
                .build();

        ItemRequest request = ItemRequest.builder()
                .id(10L)
                .description("Need a drill")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        request.addItem(item);

        when(itemRequestRepository.findByIdWithItems(10L))
                .thenReturn(Optional.of(request));

        RequestDto result = requestService.getRequest(1L, 10L);

        assertThat(result.getId(), equalTo(10L));
        assertThat(result.getItems(), hasSize(1));
        assertThat(result.getItems().getFirst().getName(), equalTo("Drill"));
    }

    @Test
    void getRequest_notFound() {
        when(itemRequestRepository.findByIdWithItems(10L))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> requestService.getRequest(1L, 10L));
    }
}

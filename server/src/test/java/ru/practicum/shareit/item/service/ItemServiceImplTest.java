package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void addNewItem_success_withoutRequest() {
        User owner = User.builder().id(1L).name("Ivan").build();

        NewItemRequestDto dto = new NewItemRequestDto();
        dto.setName("Drill");
        dto.setDescription("Powerful");
        dto.setAvailable(true);

        Item savedItem = Item.builder()
                .id(1L)
                .name("Drill")
                .description("Powerful")
                .available(true)
                .owner(owner)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.save(any(Item.class))).thenReturn(savedItem);

        ItemDto result = itemService.addNewItem(1L, dto);

        assertThat(result, allOf(
                hasProperty("id", equalTo(1L)),
                hasProperty("name", equalTo("Drill")),
                hasProperty("available", equalTo(true)),
                hasProperty("requestId", nullValue())
        ));

        verify(itemRepository).save(any(Item.class));
    }

    @Test
    void addNewItem_userNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.addNewItem(1L, new NewItemRequestDto()));
    }

    @Test
    void addNewItem_withOwnRequest_throwsException() {
        User owner = User.builder().id(1L).build();
        ItemRequest request = ItemRequest.builder().id(10L).requestor(owner).build();

        NewItemRequestDto dto = new NewItemRequestDto();
        dto.setRequestId(10L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRequestRepository.findById(10L)).thenReturn(Optional.of(request));

        assertThrows(ConditionsNotMetException.class,
                () -> itemService.addNewItem(1L, dto));
    }

    @Test
    void updateItem_success() {
        User owner = User.builder().id(1L).build();
        Item item = Item.builder()
                .id(1L)
                .name("Old")
                .description("Old")
                .available(false)
                .owner(owner)
                .build();

        UpdateItemRequestDto dto = new UpdateItemRequestDto();
        dto.setName("New");
        dto.setAvailable(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);

        ItemDto result = itemService.updateItem(1L, 1L, dto);

        assertThat(result, allOf(
                hasProperty("name", equalTo("New")),
                hasProperty("available", equalTo(true))
        ));
    }

    @Test
    void updateItem_notOwner() {
        User owner = User.builder().id(2L).build();
        User another = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).owner(owner).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(another));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(ConditionsNotMetException.class,
                () -> itemService.updateItem(1L, 1L, new UpdateItemRequestDto()));
    }


    @Test
    void getItemById_success_owner() {
        User owner = User.builder().id(1L).name("Ivan").build();
        Item item = Item.builder().id(1L).name("Drill").owner(owner).build();

        Comment comment = Comment.builder()
                .id(1L)
                .text("Good")
                .author(owner)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(commentRepository.findByItemId(1L)).thenReturn(List.of(comment));
        when(bookingRepository.findByItemIdAndStatusOrderByStartAsc(1L, Status.APPROVED))
                .thenReturn(List.of());

        ItemDto result = itemService.getItemById(1L, 1L);

        assertThat(result.getComments(), hasSize(1));
        assertThat(result.getComments().getFirst().getText(), equalTo("Good"));
    }

    @Test
    void getItemById_notFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void searchItems_blankQuery_returnsEmpty() {
        List<ItemDto> result = itemService.searchItems("   ");

        assertThat(result, empty());
        verify(itemRepository, never()).search(any());
    }


    @Test
    void addComment_success() {
        User user = User.builder().id(1L).name("Ivan").build();
        Item item = Item.builder().id(1L).build();

        NewCommentDto dto = new NewCommentDto();
        dto.setText("Nice");

        Comment saved = Comment.builder()
                .id(1L)
                .text("Nice")
                .author(user)
                .item(item)
                .created(LocalDateTime.now())
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(
                eq(1L), eq(1L), eq(Status.APPROVED), any(LocalDateTime.class)))
                .thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(saved);

        CommentDto result = itemService.addComment(1L, 1L, dto);

        assertThat(result, allOf(
                hasProperty("text", equalTo("Nice")),
                hasProperty("authorName", equalTo("Ivan"))
        ));
    }

    @Test
    void addComment_withoutBooking_throwsException() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(User.builder().id(1L).build()));
        when(itemRepository.findById(1L))
                .thenReturn(Optional.of(Item.builder().id(1L).build()));
        when(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndBefore(
                anyLong(), anyLong(), eq(Status.APPROVED), any(LocalDateTime.class)))
                .thenReturn(false);

        assertThrows(ConditionsNotMetException.class,
                () -> itemService.addComment(1L, 1L, new NewCommentDto()));
    }
}

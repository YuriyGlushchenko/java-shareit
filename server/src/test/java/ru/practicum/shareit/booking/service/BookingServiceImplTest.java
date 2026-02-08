package ru.practicum.shareit.booking.service;

import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.querydsl.QSort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyList;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void addNewBooking_success() {
        User owner = User.builder().id(1L).name("Owner").build();
        User booker = User.builder().id(2L).name("Booker").build();

        Item item = Item.builder()
                .id(10L)
                .name("Item")
                .available(true)
                .owner(owner)
                .build();

        NewBookingRequestDto request = NewBookingRequestDto.builder()
                .itemId(10L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        Booking savedBooking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .start(request.getStart())
                .end(request.getEnd())
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));
        when(bookingRepository.existsOverlappingBooking(
                eq(10L),
                eq(request.getStart()),
                eq(request.getEnd()),
                anyList()
        )).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(savedBooking);

        BookingDto result = bookingService.addNewBooking(2L, request);

        assertThat(result.getId(), equalTo(100L));
        assertThat(result.getStatus(), equalTo(Status.WAITING));
        assertThat(result.getItem().getId(), equalTo(10L));
        assertThat(result.getBooker().getId(), equalTo(2L));

        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void addNewBooking_itemNotAvailable_throwsException() {
        User owner = User.builder().id(1L).build();
        User booker = User.builder().id(2L).build();

        Item item = Item.builder()
                .id(10L)
                .available(false)
                .owner(owner)
                .build();

        NewBookingRequestDto request = NewBookingRequestDto.builder()
                .itemId(10L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(booker));
        when(itemRepository.findById(10L)).thenReturn(Optional.of(item));

        assertThrows(ConditionsNotMetException.class,
                () -> bookingService.addNewBooking(2L, request));
    }

    @Test
    void approve_success() {
        User owner = User.builder().id(1L).build();
        User booker = User.builder().id(2L).build();

        Item item = Item.builder().id(10L).owner(owner).build();

        Booking booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingDto result = bookingService.approve(100L, 1L, true);

        assertThat(result.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void getBookingById_success_forOwner() {
        User owner = User.builder().id(1L).build();
        User booker = User.builder().id(2L).build();

        Item item = Item.builder().id(10L).owner(owner).build();

        Booking booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .build();

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        BookingDto result = bookingService.getBookingById(1L, 100L);

        assertThat(result.getId(), equalTo(100L));
    }

    @Test
    void getBookingById_notAllowed_throwsException() {
        User owner = User.builder().id(1L).build();
        User booker = User.builder().id(2L).build();
        User stranger = User.builder().id(3L).build();

        Item item = Item.builder().id(10L).owner(owner).build();

        Booking booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(booker)
                .build();

        when(bookingRepository.findById(100L)).thenReturn(Optional.of(booking));
        when(userRepository.findById(3L)).thenReturn(Optional.of(stranger));

        assertThrows(ConditionsNotMetException.class,
                () -> bookingService.getBookingById(3L, 100L));
    }

    @Test
    void getAllUserBookings_success() {
        User user = User.builder()
                .id(1L)
                .name("User")
                .build();

        Item item = Item.builder()
                .id(10L)
                .name("Item")
                .owner(user)
                .build();

        Booking booking = Booking.builder()
                .id(100L)
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.findAll(
                any(BooleanBuilder.class),
                any(QSort.class)
        )).thenReturn(List.of(booking));

        List<BookingDto> result =
                bookingService.getAllUserBookings(1L, State.ALL);

        assertThat(result, hasSize(1));
        assertThat(result.getFirst().getId(), equalTo(100L));
        assertThat(result.getFirst().getItem().getId(), equalTo(10L));
        assertThat(result.getFirst().getBooker().getId(), equalTo(1L));
    }

    @Test
    void getOwnerItemsBookings_userNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.getOwnerItemsBookings(1L, State.ALL));
    }
}

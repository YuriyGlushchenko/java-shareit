package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingRequestDto;
import ru.practicum.shareit.booking.model.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto addNewBooking(Long userId, NewBookingRequestDto bookingRequest) {
        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        Item item = itemRepository.findById(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + bookingRequest.getItemId() + "не найдена."));

        if (!item.getAvailable()) {
            throw new ConditionsNotMetException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(userId)) {
            throw new ConditionsNotMetException("Владелец не может бронировать свою вещь");
        }

        if (bookingRepository.existsOverlappingBooking(
                bookingRequest.getItemId(),
                bookingRequest.getStart(),
                bookingRequest.getEnd(),
                List.of(Status.APPROVED, Status.WAITING))) {
            throw new ConditionsNotMetException("Бронирование пересекается с уже существующим бронированием");
        }

        Booking booking = BookingMapper.mapToBooking(bookingRequest, item, booker);
        booking = bookingRepository.save(booking);

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto approve(Long bookingId, Long userId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ConditionsNotMetException("Бронирование с id: " + bookingId + "не найдено."));

        Long ownerId = booking.getItem().getOwner().getId();
        userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));


        if (!userId.equals(ownerId)) {
            throw new ConditionsNotMetException("Изменить статус бронирования может только владелец вещи");
        }

        if (booking.getStatus() != Status.WAITING) {
            throw new ConditionsNotMetException("Статус бронирования уже изменён");
        }

        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);

        return BookingMapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ConditionsNotMetException("Бронирование с id: " + bookingId + "не найдено."));

        Long ownerId = booking.getItem().getOwner().getId();

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        if (!userId.equals(ownerId) && !userId.equals(booking.getBooker().getId())) {
            throw new ConditionsNotMetException("Данные по бронированию может получить либо владелец вещи, либо тот кто бронировал.");
        }

        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getAllUserBookings(Long userId, State state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь не найден"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, now, now);
            case PAST -> bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED -> bookingRepository.findByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case ALL -> bookingRepository.findByBookerIdOrderByStartDesc(userId);
        };

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingDto> getOwnerItemsBookings(Long userId, State state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings = switch (state) {
            case CURRENT -> bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, now, now);
            case PAST -> bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, now);
            case FUTURE -> bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(userId, now);
            case WAITING -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED -> bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case ALL -> bookingRepository.findByItemOwnerIdOrderByStartDesc(userId);
        };

        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
    }

}

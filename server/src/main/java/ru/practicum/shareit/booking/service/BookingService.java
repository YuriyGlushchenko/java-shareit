package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.NewBookingRequestDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> getAllUserBookings(Long userId, State state);

    List<BookingDto> getOwnerItemsBookings(Long userId, State state);

    BookingDto addNewBooking(Long userId, NewBookingRequestDto booking);

    BookingDto approve(Long bookingId, Long userId, Boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

}

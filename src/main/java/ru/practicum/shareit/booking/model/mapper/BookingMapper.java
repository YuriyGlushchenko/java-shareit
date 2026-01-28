package ru.practicum.shareit.booking.model.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.dto.NewBookingRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingMapper {

    public static Booking mapToBooking(
            NewBookingRequestDto request,
            Item item,
            User booker
    ) {
        return Booking.builder()
                .start(request.getStart())
                .end(request.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

    public static BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(
                        BookingDto.BookingItemDto.builder()
                                .id(booking.getItem().getId())
                                .name(booking.getItem().getName())
                                .build()
                )
                .booker(
                        BookingDto.BookingUserDto.builder()
                                .id(booking.getBooker().getId())
                                .name(booking.getBooker().getName())
                                .build()
                )
                .build();
    }

    public static BookingShortDto toShortDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingShortDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}

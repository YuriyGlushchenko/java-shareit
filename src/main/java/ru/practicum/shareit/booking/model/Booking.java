package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
public class Booking {
    private Long id;

    @NotNull(message = "Дата релиза обязательна")
    @FutureOrPresent(message = "Дата начала должна быть текущей или будущей")
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent(message = "Дата начала должна быть текущей или будущей")
    private LocalDateTime end;

    @NotNull
    private Item item;

    @NotNull
    private User booker;

    @NotNull
    private Status status;


}

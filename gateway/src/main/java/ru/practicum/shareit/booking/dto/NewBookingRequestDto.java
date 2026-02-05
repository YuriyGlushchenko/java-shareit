package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.validation.ValidBookingDates;

import java.time.LocalDateTime;


@Builder
@Data
@ValidBookingDates
public class NewBookingRequestDto {

    @NotNull(message = "Дата начала обязательна")
    @FutureOrPresent(message = "Дата начала должна быть текущей или будущей")
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent(message = "Дата начала должна быть текущей или будущей")
    private LocalDateTime end;

    @NotNull
    private Long itemId;

}

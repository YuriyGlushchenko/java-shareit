package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookingItemDto {
    private Long id;
    private String name;
}
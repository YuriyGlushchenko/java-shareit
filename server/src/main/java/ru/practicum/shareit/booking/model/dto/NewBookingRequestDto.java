package ru.practicum.shareit.booking.model.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Builder
@Data
public class NewBookingRequestDto {


    private LocalDateTime start;


    private LocalDateTime end;


    private Long itemId;

}

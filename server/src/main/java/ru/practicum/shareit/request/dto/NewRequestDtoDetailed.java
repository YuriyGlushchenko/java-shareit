package ru.practicum.shareit.request.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewRequestDtoDetailed {
    private Long id;
    private String description;
    private LocalDateTime created;

}

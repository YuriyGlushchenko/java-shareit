package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class NewRequestDtoDetailed {
    private Long id;

    @NotBlank
    private String description;
    private LocalDateTime created;

}

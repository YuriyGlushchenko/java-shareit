package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class UpdateRequestDTO {
    private Long id;

    @NotBlank
    private String description;

    private User requestor;

    private LocalDateTime created;

}

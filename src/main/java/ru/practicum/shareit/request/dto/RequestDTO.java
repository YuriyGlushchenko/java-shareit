package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDTO;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */

@Builder
@Data
public class RequestDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String description;

    private UserDTO requestor;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime created;

}

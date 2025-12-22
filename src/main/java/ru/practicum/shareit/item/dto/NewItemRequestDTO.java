package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class NewItemRequestDTO {

    @NotBlank
    private String name;

    private String description;

    private Boolean available;
}
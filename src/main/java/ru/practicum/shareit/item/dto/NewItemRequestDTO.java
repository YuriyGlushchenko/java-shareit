package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class NewItemRequestDTO {

    @NotBlank
    private String name;

    @NotBlank(message = "Описание должно быть заполнено")
    private String description;

    @NotNull(message = "Поле 'available' должно быть заполнено")
    private Boolean available;
}
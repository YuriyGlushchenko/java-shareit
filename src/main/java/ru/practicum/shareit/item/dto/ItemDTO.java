package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private int shareCount;
}

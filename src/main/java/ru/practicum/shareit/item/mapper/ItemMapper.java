package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.dto.UpdateItemRequestDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemMapper {

    public static Item mapToItem(NewItemRequestDTO request, User owner) {
        Item item = Item.builder()
                .name(request.getName())
                .description(request.getDescription())
                .available(request.getAvailable())
                .ownerId(owner.getId())
                .build();

        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }

        return item;
    }

    public static ItemDTO mapToItemDto(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item updateItemFields(Item item, UpdateItemRequestDTO request) {
        if (request.hasName()) {
            item.setName(request.getName());
        }
        if (request.hasDescription()) {
            item.setDescription(request.getDescription());
        }
        if (request.hasAvailable()) {
            item.setAvailable(request.getAvailable());
        }
        return item;
    }
}
package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;

import java.util.List;

public interface ItemService {

    ItemDto addNewItem(Long userId, NewItemRequestDto itemRequestDTO);

    ItemDto updateItem(long userId, long itemId, UpdateItemRequestDto itemRequestDTO);

    ItemDto getItemById(long itemId);

    List<ItemDto> getOwnerItems(long userId);

    List<ItemDto> searchItems(String qury);


}

package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;

public interface ItemService {

    ItemDTO addNewItem(Long userId, NewItemRequestDTO itemRequestDTO);
}

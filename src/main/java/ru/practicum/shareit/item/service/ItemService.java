package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.dto.UpdateItemRequestDTO;

import java.util.List;

public interface ItemService {

    ItemDTO addNewItem(Long userId, NewItemRequestDTO itemRequestDTO);

    ItemDTO updateItem(long userId, long itemId, UpdateItemRequestDTO itemRequestDTO);

    ItemDTO getItemById(long itemId);

    List<ItemDTO> getOwnerItems(long userId);

    List<ItemDTO> searchItems(String qury);


}

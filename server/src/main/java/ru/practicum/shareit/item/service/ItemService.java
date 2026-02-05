package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDto addNewItem(Long userId, NewItemRequestDto itemRequestDTO);

    ItemDto updateItem(long userId, long itemId, UpdateItemRequestDto itemRequestDTO);

    ItemDto getItemById(Long itemId, Long userId);

    List<ItemDto> getOwnerItems(long userId);

    List<ItemDto> searchItems(String qury);

    CommentDto addComment(Long userId, Long itemId, NewCommentDto dto);


}

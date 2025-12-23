package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.dto.UpdateItemRequestDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userRepository;
    private final ItemStorage itemRepository;

    @Override
    public ItemDTO addNewItem(Long userId, NewItemRequestDTO itemRequestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));

        Item item = ItemMapper.mapToItem(itemRequestDTO, owner);
        item = itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDTO updateItem(long userId, long itemId, UpdateItemRequestDTO requestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));

        if (owner.getId() != userId) {
            throw new ConditionsNotMetException("Изменять вещь может только ее владелец");
        }

        Item item = itemRepository.findById(itemId)
                .map(i -> ItemMapper.updateItemFields(i, requestDTO))
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + "не найдена"));

        item = itemRepository.update(item);
        ItemDTO itemDto = ItemMapper.mapToItemDto(item);
        setSharingCount(itemDto);

        return itemDto;

    }

    private void setSharingCount(ItemDTO itemDTO) {
        // toDo допилить в след. спринтах

        int count = itemRepository.getSharingCount(itemDTO.getId());
        itemDTO.setShareCount(count);
    }

    @Override
    public ItemDTO getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь c id: " + itemId + " не найдена"));
    }

    @Override
    public List<ItemDTO> searchItems(String qury) {
        if (qury == null || qury.isBlank()) {
            return List.of();
        }

        return itemRepository.search(qury.toLowerCase())
                .stream()
                .map(ItemMapper::mapToItemDto)
                .peek(this::setSharingCount)
                .toList();
    }

    @Override
    public List<ItemDTO> getOwnerItems(long userId) {
        return itemRepository.getItemsByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .peek(this::setSharingCount)
                .toList();
    }
}

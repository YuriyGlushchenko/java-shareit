package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.NewItemRequestDto;
import ru.practicum.shareit.item.dto.UpdateItemRequestDto;
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
    public ItemDto addNewItem(Long userId, NewItemRequestDto itemRequestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));

        Item item = ItemMapper.mapToItem(itemRequestDTO, owner);
        item = itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequestDto requestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));

        if (owner.getId() != userId) {
            throw new ConditionsNotMetException("Изменять вещь может только ее владелец");
        }

        Item item = itemRepository.findById(itemId)
                .map(i -> ItemMapper.updateItemFields(i, requestDTO))
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + "не найдена"));

        item = itemRepository.update(item);

        return ItemMapper.mapToItemDto(item);

    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemMapper::mapToItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь c id: " + itemId + " не найдена"));
    }

    @Override
    public List<ItemDto> searchItems(String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }

        return itemRepository.search(query.toLowerCase())
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getOwnerItems(long userId) {
        return itemRepository.getItemsByOwnerId(userId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }
}

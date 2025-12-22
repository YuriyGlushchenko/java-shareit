package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.NewItemRequestDTO;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserStorage userRepository;

    @Override
    public ItemDTO addNewItem(Long userId, NewItemRequestDTO itemRequestDTO) {
//        User author = userRepository.findById(newPostRequest.getAuthorId())
//                .orElseThrow(() -> new ConditionsNotMetException("Указанный автор не найден"));
//
//        Post post = PostMapper.mapToPost(newPostRequest, author);
//
//        postRepository.save(post);
//
//        return PostMapper.mapToPostDto(post);

        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new ConditionsNotMetException("Пользователь с id: " + userId + "не найден."));

        Item item = ItemMapper.mapToItem(itemRequestDTO, owner);

        return ItemMapper.mapToItemDto(item);
    }
}

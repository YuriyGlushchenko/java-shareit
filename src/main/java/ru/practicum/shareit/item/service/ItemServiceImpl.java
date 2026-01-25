package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto addNewItem(Long userId, NewItemRequestDto itemRequestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        Item item = ItemMapper.mapToItem(itemRequestDTO, owner);
        item = itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequestDto requestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        if (!owner.getId().equals(userId)) {
            throw new ConditionsNotMetException("Изменять вещь может только ее владелец");
        }

        Item item = itemRepository.findById(itemId)
                .map(i -> ItemMapper.updateItemFields(i, requestDTO))
                .orElseThrow(() -> new NotFoundException("Вещь с id: " + itemId + "не найдена"));

        item = itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);

    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        List<CommentDto> comments = commentRepository.findByItemId(itemId)
                .stream()
                .map(CommentMapper::toDto)
                .toList();

        Booking last = null;
        Booking next = null;

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime now = LocalDateTime.now();

            last = bookingRepository
                    .findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(
                            itemId, Status.APPROVED, now);

            next = bookingRepository
                    .findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                            itemId, Status.APPROVED, now);
        }

        return ItemMapper.mapToItemDto(item, last, next, comments);
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
    @Transactional(readOnly = true)
    public List<ItemDto> getOwnerItems(long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        LocalDateTime now = LocalDateTime.now();

        return items.stream()
                .map(item -> {
                    Booking last = bookingRepository
                            .findFirstByItemIdAndStatusAndEndBeforeOrderByEndDesc(
                                    item.getId(), Status.APPROVED, now);

                    Booking next = bookingRepository
                            .findFirstByItemIdAndStatusAndStartAfterOrderByStartAsc(
                                    item.getId(), Status.APPROVED, now);

                    List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                            .stream()
                            .map(CommentMapper::toDto)
                            .toList();

                    return ItemMapper.mapToItemDto(item, last, next, comments);
                })
                .toList();
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, NewCommentDto dto) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        boolean hasBooking = bookingRepository
                .existsByBookerIdAndItemIdAndStatusAndEndBefore(
                        userId, itemId, Status.APPROVED, LocalDateTime.now()
                );

        if (!hasBooking) {
            throw new ConditionsNotMetException("Пользователь не брал эту вещь в аренду");
        }

        Comment comment = CommentMapper.toComment(dto, item, author);
        comment = commentRepository.save(comment);

        return CommentMapper.toDto(comment);
    }


}

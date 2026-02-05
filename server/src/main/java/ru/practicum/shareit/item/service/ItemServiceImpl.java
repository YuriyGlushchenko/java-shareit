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
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDto addNewItem(Long userId, NewItemRequestDto itemRequestDTO) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        Long requestId = itemRequestDTO.getRequestId();
        ItemRequest request = null;
        if(requestId != null){
            request = itemRequestRepository.findById(requestId)
                    .orElseThrow(() -> new NotFoundException("Запрос на вещь с id: " + requestId + "не найден."));

            if(request.getRequestor().getId().equals(userId)){
                throw new ConditionsNotMetException("Пользователь не может отвечать на свой же запрос");
            }
        }

        Item item = ItemMapper.mapToItem(itemRequestDTO, owner);

        // т.к. связь bi-directional и хозяин связи item, то присваиваем тут и через переопределенный сеттер, а не в мапере
        if (request != null) {
            item.setRequest(request);
        }

        item = itemRepository.save(item);

        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto updateItem(long userId, long itemId, UpdateItemRequestDto requestDTO) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + " не найден."));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new ConditionsNotMetException("Изменять вещь может только владелец");
        }

        ItemMapper.updateItemFields(item, requestDTO);
        itemRepository.save(item);

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
            List<Booking> approvedBookings = bookingRepository
                    .findByItemIdAndStatusOrderByStartAsc(itemId, Status.APPROVED);

            LocalDateTime now = LocalDateTime.now();

            for (Booking booking : approvedBookings) {
                if (booking.getEnd().isBefore(now)) {
                    last = booking;
                } else if (booking.getStart().isAfter(now)) {
                    next = booking;
                    break;
                }
            }
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
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream()
                .map(Item::getId)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        // все бронирования разом для всех id и собираем в map по id вещи
        List<Booking> allBookings = bookingRepository.findAllBookingsForItems(itemIds, Status.APPROVED);
        Map<Long, List<Booking>> bookingsByItem = allBookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));


        //  все комменты разом, сразу собираем в map по id вещи
        Map<Long, List<CommentDto>> commentsMap = commentRepository
                .findByItemIdIn(itemIds)
                .stream()
                .collect(Collectors.groupingBy(
                        comment -> comment.getItem().getId(),  // группировка по ключу = id вещи
                        Collectors.mapping(CommentMapper::toDto, Collectors.toList())  // значение мапим в список DTO
                ));

        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItem.getOrDefault(
                            item.getId(), Collections.emptyList());

                    Booking last = null;
                    Booking next = null;

                    // Находим последнее и следующее бронирование
                    for (Booking booking : itemBookings) {
                        if (booking.getEnd().isBefore(now)) {
                            if (last == null || booking.getEnd().isAfter(last.getEnd())) {
                                last = booking;
                            }
                        } else if (booking.getStart().isAfter(now)) {
                            if (next == null || booking.getStart().isBefore(next.getStart())) {
                                next = booking;
                            }
                        }
                    }

                    List<CommentDto> comments = commentsMap.getOrDefault(
                            item.getId(), Collections.emptyList());

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

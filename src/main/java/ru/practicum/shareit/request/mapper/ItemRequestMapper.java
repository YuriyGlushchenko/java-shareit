package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.NewRequestDtoDetailed;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(NewRequestDtoDetailed request, User requestor) {
        return ItemRequest.builder()
                .description(request.getDescription())
                .created(LocalDateTime.now())
                .requestor(requestor)
                .build();
    }

    public static RequestDto mapToFullRequestDto(ItemRequest itemRequest) {
        RequestDto dto = mapToBaseRequestDto(itemRequest);

        dto.setAnswers(mapToAnswerDtos(itemRequest.getItems()));

        return dto;
    }

    public static RequestDto mapToBaseRequestDto(ItemRequest itemRequest) {
        RequestDto dto = RequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

        dto.setRequestor(UserMapper.mapToUserDto(itemRequest.getRequestor()));
        return dto;
    }

    private static List<RequestDto.AnswerDto> mapToAnswerDtos(List<Item> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(ItemRequestMapper::mapToAnswerDto)
                .collect(Collectors.toList());
    }

    private static RequestDto.AnswerDto mapToAnswerDto(Item item) {
        if (item == null) {
            return null;
        }

        return RequestDto.AnswerDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner() != null ? item.getOwner().getId() : null)
                .build();
    }
}
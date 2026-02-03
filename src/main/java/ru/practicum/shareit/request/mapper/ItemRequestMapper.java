package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequest mapToItemRequest(NewRequestDto request, User requestor) {
        return ItemRequest.builder()
                .description(request.getDescription())
                .created(LocalDateTime.now())
                .requestor(requestor)
                .build();
    }

    public static RequestDto mapToRequestDto(ItemRequest itemRequest) {
        RequestDto dto = RequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

        User requestor = itemRequest.getRequestor();
        dto.setRequestor(UserMapper.mapToUserDto(requestor));

        return dto;
    }
}
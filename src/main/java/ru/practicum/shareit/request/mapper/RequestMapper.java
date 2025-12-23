package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.dto.NewRequestDTO;
import ru.practicum.shareit.request.dto.RequestDTO;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {
    public static ItemRequest mapToItemRequest(NewRequestDTO request, User requestor) {
        return ItemRequest.builder()
                .description(request.getDescription())
                .created(LocalDateTime.now())
                .requestor(requestor)
                .build();
    }

    public static RequestDTO mapToRequestDto(ItemRequest itemRequest) {
        RequestDTO dto = RequestDTO.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();

        User requestor = itemRequest.getRequestor();
        dto.setRequestor(UserMapper.mapToUserDto(requestor));

        return dto;
    }
}
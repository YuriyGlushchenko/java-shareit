package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.NewRequestDtoDetailed;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface ItemRequestService {
    RequestDto addNewRequest(Long userId, NewRequestDtoDetailed request);

    List<RequestDto> getUserRequests(Long userId);

    List<RequestDto> getOtherUsersRequests(Long userId);

    RequestDto getRequest(Long userId, Long requestId);
}

package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface ItemRequestService {
    RequestDto addNewRequest(Long userId, NewRequestDto request);

    List<RequestDto> getUserRequests(Long userId);

    List<RequestDto> getRequests(Long userId);

    List<RequestDto> getRequest(Long userId, Long requestId);
}

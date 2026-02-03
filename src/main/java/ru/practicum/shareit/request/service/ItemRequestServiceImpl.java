package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.NewRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public RequestDto addNewRequest(Long userId, NewRequestDto request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(request, user));

        return ItemRequestMapper.mapToRequestDto(itemRequest);
    }

    @Override
    public List<RequestDto> getUserRequests(Long userId) {
        return List.of();
    }

    @Override
    public List<RequestDto> getRequests(Long userId) {
        return List.of();
    }

    @Override
    public List<RequestDto> getRequest(Long userId, Long requestId) {
        return List.of();
    }
}

package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.NewRequestDtoDetailed;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public RequestDto addNewRequest(Long userId, NewRequestDtoDetailed request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(request, user));

        return ItemRequestMapper.mapToBaseRequestDto(itemRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getUserRequests(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdWithItems(userId);

        return requests.stream()
                .map(ItemRequestMapper::mapToFullRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestDto> getRequests(Long userId) {
        //  Возможно, тут эта проверка и не нужна? Незарегистрированный пользователь пусть смотрит запросы?
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + userId + "не найден."));

        // По идее надо еще пагинацию. Но в ТЗ нет, да и параметры from и size не приходят в запросе
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdNotOrderByCreatedDesc(userId);

        return requests.stream()
                .map(ItemRequestMapper::mapToBaseRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RequestDto getRequest(Long userId, Long requestId) {
        ItemRequest request = itemRequestRepository.findByIdWithItems(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос вещи с id: " + requestId + " не найден."));

        return ItemRequestMapper.mapToFullRequestDto(request);
    }
}

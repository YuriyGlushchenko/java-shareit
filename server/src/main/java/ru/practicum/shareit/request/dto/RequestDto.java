package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class RequestDto {
    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<RequestItemDtoShort> items;

    @Data
    @Builder
    public static class RequestItemDtoShort {
        private Long id;
        private String name;
        private Long ownerId;
    }
}


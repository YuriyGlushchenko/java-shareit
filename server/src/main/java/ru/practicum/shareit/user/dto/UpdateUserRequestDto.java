package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String email;

    private String name;

    public boolean hasNname() {
        return !(name == null || name.isBlank());
    }

    public boolean hasEmail() {
        return !(email == null || email.isBlank());
    }
}

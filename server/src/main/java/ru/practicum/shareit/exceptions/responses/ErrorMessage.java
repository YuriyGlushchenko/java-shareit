package ru.practicum.shareit.exceptions.responses;

import lombok.Data;

@Data
public class ErrorMessage {
    private final String error;
    private final String message;

}

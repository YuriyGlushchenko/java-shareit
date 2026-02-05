package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum State {
    ALL("all"),
    CURRENT("current"),
    PAST("past"),
    FUTURE("future"),
    WAITING("waiting"),
    REJECTED("rejected");

    private final String value;

    State(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public static State from(String state) {
        if (state == null || state.isBlank()) {
            return ALL;
        }

        String normalizedState = state.toLowerCase().trim();

        return switch (normalizedState) {
            case "all" -> ALL;
            case "current" -> CURRENT;
            case "past" -> PAST;
            case "future" -> FUTURE;
            case "waiting" -> WAITING;
            case "rejected" -> REJECTED;
            default -> throw new IllegalArgumentException("Unknown state: " + state);
        };

    }
}

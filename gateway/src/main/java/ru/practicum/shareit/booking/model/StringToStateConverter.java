package ru.practicum.shareit.booking.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStateConverter implements Converter<String, State> {

    @Override
    public State convert(String source) {
        return State.from(source);
    }
}

package ru.practicum.shareit.booking.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.NewBookingRequestDto;

public class BookingDatesValidator
        implements ConstraintValidator<ValidBookingDates, NewBookingRequestDto> {

    @Override
    public boolean isValid(NewBookingRequestDto dto,
                           ConstraintValidatorContext context) {

        if (dto == null) {
            return true;
        }

        if (dto.getStart() == null || dto.getEnd() == null) {
            return true; // @NotNull отработает отдельно
        }

        return dto.getStart().isBefore(dto.getEnd());
    }
}
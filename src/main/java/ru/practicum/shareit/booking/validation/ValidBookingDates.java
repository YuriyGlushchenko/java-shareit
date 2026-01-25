package ru.practicum.shareit.booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // Валидация всего объекта, т.к. сравниваем два поля объекта
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BookingDatesValidator.class)
@Documented
public @interface ValidBookingDates {

    String message() default "Дата окончания должна быть позже даты начала";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
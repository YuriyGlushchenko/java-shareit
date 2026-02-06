package ru.practicum.shareit.exceptions;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exceptions.exceptions.ConditionsNotMetException;
import ru.practicum.shareit.exceptions.exceptions.DuplicatedDataException;
import ru.practicum.shareit.exceptions.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.exceptions.ValidationException;
import ru.practicum.shareit.exceptions.responses.ErrorMessage;
import ru.practicum.shareit.exceptions.responses.ValidationError;
import ru.practicum.shareit.exceptions.responses.ValidationErrorResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
// Автоматически добавляет @ResponseBody ко всем методам. Возвращаемые объекты автоматически сериализуются в JSON.
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Перехват исключения при валидации аргументов тела запроса с @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // MethodArgumentNotValidException — исключение, которое выбрасывается Spring при неудачной валидации с @Valid

        // Получаем все ошибки валидации полей
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<ValidationError> validationErrors = fieldErrors.stream()
                .map(fieldError -> new ValidationError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()
                ))
                .collect(Collectors.toList());

        return new ValidationErrorResponse("VALIDATION_FAILED", validationErrors);
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(ValidationException ex) {

        List<ValidationError> validationErrors = List.of(
                new ValidationError(ex.getFieldName(), ex.getMessage(), ex.getRejectedValue()));

        return new ValidationErrorResponse("VALIDATION_FAILED", validationErrors);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleNotFoundException(NotFoundException ex) {

        return new ErrorMessage("NOT_FOUND", ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        return new ErrorMessage("BAD_REQUEST", "Required request body is missing");
    }

    @ExceptionHandler(DuplicatedDataException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleDuplicatedDataException(DuplicatedDataException ex) {

        return new ErrorMessage("CONFLICT", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleException(Exception ex) {

        return new ErrorMessage("INTERNAL_SERVER_ERROR", ex.getMessage());
    }

    @ExceptionHandler(ConditionsNotMetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleConditionsNotMetException(ConditionsNotMetException ex) {

        return new ErrorMessage("UNPROCESSABLE_ENTITY", ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        return new ErrorMessage("CONFLICT", ex.getMessage());
    }


}


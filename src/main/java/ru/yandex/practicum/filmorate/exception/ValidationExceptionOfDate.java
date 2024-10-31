package ru.yandex.practicum.filmorate.exception;

public class ValidationExceptionOfDate extends RuntimeException {
    public ValidationExceptionOfDate(String message) {
        super(message);
    }
}

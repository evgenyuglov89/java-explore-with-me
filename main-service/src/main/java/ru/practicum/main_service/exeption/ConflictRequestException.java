package ru.practicum.main_service.exeption;

public class ConflictRequestException extends RuntimeException {
    public ConflictRequestException(String message) {
        super(message);
    }
}

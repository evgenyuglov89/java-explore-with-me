package ru.practicum.main_service.exeption;

public class CompilationNotFoundException extends RuntimeException {
    public CompilationNotFoundException(String message) {
        super(message);
    }
}

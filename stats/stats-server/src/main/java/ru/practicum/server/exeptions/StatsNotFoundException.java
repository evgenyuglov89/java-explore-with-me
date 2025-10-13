package ru.practicum.server.exeptions;

public class StatsNotFoundException extends RuntimeException {
    public StatsNotFoundException(String message) {
        super(message);
    }
}

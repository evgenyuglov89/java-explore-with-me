package ru.practicum.main_service.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFound(EventNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Событие не найдено", ex, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Пользователь не найден", ex, request);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRequestNotFound(RequestNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Запрос не найден", ex, request);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Категория не найдена", ex, request);
    }

    @ExceptionHandler(CompilationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCompilationNotFound(CompilationNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Подборка не найдена", ex, request);
    }

    @ExceptionHandler(ConflictRequestException.class)
    public ResponseEntity<ErrorResponse> handleConflictRequest(ConflictRequestException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Конфликт с текущим состоянием сервера", ex, request);
    }

    @ExceptionHandler(IncorrectRequestException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectRequest(IncorrectRequestException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Некорректные данные", ex, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка сервера", ex, request);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatus status, String error, Exception ex,
                                                              WebRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(response, status);
    }
}

package ru.practicum.server.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StatsNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleItemNotFound(StatsNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Некорректный запрос", ex, request);
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

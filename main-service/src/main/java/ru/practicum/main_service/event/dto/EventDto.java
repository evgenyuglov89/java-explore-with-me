package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.TIME_FORMAT;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    @NotNull
    private int id;

    @NotNull
    private String title;

    @NotNull
    private String annotation;

    @NotNull
    private String description;

    @NotNull
    private CategoryDto category;

    @NotNull
    private UserDto initiator;

    @NotNull
    private boolean paid;

    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime createdOn;

    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime publishedOn;

    @NotNull
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private int confirmedRequests;

    @NotNull
    private Location location;

    @PositiveOrZero
    private int participantLimit;

    @NotNull
    private boolean requestModeration;

    private EventState state;

    private int views;
}
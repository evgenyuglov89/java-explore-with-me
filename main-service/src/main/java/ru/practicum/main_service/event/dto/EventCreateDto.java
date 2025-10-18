package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.location.model.Location;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.TIME_FORMAT;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
    private String title;

    private String annotation;

    private String description;

    private int category;

    private Boolean paid;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private int participantLimit;

    private Location location;

    private Boolean requestModeration;
}

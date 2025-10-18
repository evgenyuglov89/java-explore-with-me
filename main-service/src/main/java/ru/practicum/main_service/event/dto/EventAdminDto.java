package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.event.model.StateAdminAction;
import ru.practicum.main_service.location.dto.LocationDto;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.TIME_FORMAT;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventAdminDto {
    private String title;

    private String annotation;

    private String description;

    private int category;

    private LocationDto location;

    private Boolean paid;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private Integer participantLimit;

    private Boolean requestModeration;

    private StateAdminAction stateAction;
}

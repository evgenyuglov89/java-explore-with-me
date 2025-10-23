package ru.practicum.main_service.event.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.main_service.event.model.StateAction;
import ru.practicum.main_service.location.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.DATE_FORMAT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateDto {
    @Size(min = 3, max = 120)
    private String title;

    @Size(min = 20, max = 2000)
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    private int category;

    private Boolean paid;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    @PositiveOrZero
    private int participantLimit;

    private Location location;

    private Boolean requestModeration;

    private StateAction stateAction;
}
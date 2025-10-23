package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.practicum.main_service.location.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.DATE_FORMAT;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateDto {
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotNull
    @Positive
    private int category;

    private Boolean paid;

    @JsonFormat(pattern = DATE_FORMAT)
    @Future
    private LocalDateTime eventDate;

    @PositiveOrZero
    private int participantLimit;

    @NotNull
    private Location location;

    private Boolean requestModeration;
}

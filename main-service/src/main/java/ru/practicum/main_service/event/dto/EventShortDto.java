package ru.practicum.main_service.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.user.dto.UserDto;

import java.time.LocalDateTime;

import static ru.practicum.main_service.consts.Consts.DATE_FORMAT;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private int id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private UserDto initiator;

    private boolean paid;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime eventDate;

    private int views;

    private int confirmedRequests;

    private EventState state;

    private int commentsCount;
}

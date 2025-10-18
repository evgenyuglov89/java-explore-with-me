package ru.practicum.main_service.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private int id;

    private String title;

    private String annotation;

    private CategoryDto category;

    private UserDto initiator;

    private boolean paid;

    private LocalDateTime eventDate;

    private int views;

    private int confirmedRequests;

    private EventState state;
}

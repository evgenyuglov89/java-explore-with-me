package ru.practicum.main_service.event.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.mapper.UserMapper;

@Component
@AllArgsConstructor
public class EventMapper {
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;

    public EventDto toEventDto(Event event) {
        return EventDto
                .builder()
                .location(event.getLocation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .confirmedRequests(event.getConfirmedRequests())
                .id(event.getId())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .paid(event.isPaid())
                .requestModeration(event.isRequestModeration())
                .initiator(userMapper.toUserDto(event.getInitiator()))
                .description(event.getDescription())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .participantLimit(event.getParticipantLimit())
                .annotation(event.getAnnotation())
                .build();
    }

    public Event fromEventCreateDto(EventCreateDto dto) {
        return Event.builder()
                .location(dto.getLocation())
                .eventDate(dto.getEventDate())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .participantLimit(dto.getParticipantLimit())
                .annotation(dto.getAnnotation())
                .build();
    }

    public EventShortDto toEventSmallDto(Event event) {
        return EventShortDto.builder()
                .eventDate(event.getEventDate())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .title(event.getTitle())
                .id(event.getId())
                .initiator(userMapper.toUserDto(event.getInitiator()))
                .views(event.getViews())
                .paid(event.isPaid())
                .build();
    }
}
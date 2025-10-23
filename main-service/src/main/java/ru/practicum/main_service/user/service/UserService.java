package ru.practicum.main_service.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.EventUpdateDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.model.StateAction;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.*;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.repository.LocationRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    public EventDto createEvent(int userId, EventCreateDto eventDto) {
        checkEventDate(eventDto.getEventDate());

        User initiator = getUserById(userId);
        Category category = getCategoryById(eventDto.getCategory());
        Location location = resolveLocation(eventDto.getLocation());

        Event event = eventMapper.fromEventCreateDto(eventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(EventState.PENDING);
        event.setCreatedOn(LocalDateTime.now());
        event.setLocation(location);

        event.setPaid(Optional.ofNullable(eventDto.getPaid()).orElse(false));
        event.setRequestModeration(Optional.ofNullable(eventDto.getRequestModeration()).orElse(true));

        return eventMapper.toEventDto(eventRepository.save(event));
    }

    public List<EventShortDto> getEventsByUser(int userId, int from, int size) {
        User user = getUserById(userId);
        var events = eventRepository.getByInitiator(user, PageRequest.of(from / size, size));
        return events.stream()
                .map(eventMapper::toEventSmallDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventByInitiator(int userId, int eventId) {
        Event event = getEventByInitiatorAndId(userId, eventId);
        return eventMapper.toEventDto(event);
    }

    public EventDto changeEvent(int userId, int eventId, EventUpdateDto dto) {
        if (dto.getEventDate() != null) checkEventDate(dto.getEventDate());

        Event event = getEventByInitiatorAndId(userId, eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("Изменить можно только события в состоянии ожидания модерации");
        }

        updateEventFields(event, dto);
        updateStateByAction(event, dto.getStateAction());

        return eventMapper.toEventDto(eventRepository.save(event));
    }

    private Event getEventByInitiatorAndId(int userId, int eventId) {
        User initiator = getUserById(userId);
        Event event = eventRepository.getByInitiatorAndId(initiator, eventId);
        if (event == null) {
            throw new EventNotFoundException("Событие с userId " + userId + " не найдено");
        }
        return event;
    }

    private void updateEventFields(Event event, EventUpdateDto dto) {
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != 0) event.setCategory(getCategoryById(dto.getCategory()));
        if (dto.getLocation() != null) event.setLocation(resolveLocation(dto.getLocation()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != 0) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
    }

    private void updateStateByAction(Event event, StateAction action) {
        if (action == null) return;
        switch (action) {
            case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
            case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
        }
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(3))) {
            throw new IncorrectRequestException("Ивент должен быть минимум через 3 часа от текущего времени");
        }
    }

    private Location resolveLocation(Location location) {
        if (location == null) return null;
        return Optional.ofNullable(locationRepository.getByLatAndLon(location.getLat(), location.getLon()))
                .orElseGet(() -> locationRepository.save(location));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id " + id + " не найдена"));
    }
}

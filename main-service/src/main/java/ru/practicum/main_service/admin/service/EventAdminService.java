package ru.practicum.main_service.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.dto.EventAdminDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.model.StateAdminAction;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.ConflictRequestException;
import ru.practicum.main_service.exeption.EventNotFoundException;
import ru.practicum.main_service.exeption.IncorrectRequestException;
import ru.practicum.main_service.location.dto.LocationDto;
import ru.practicum.main_service.location.mapper.LocationMapper;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.location.repository.LocationRepository;
import ru.practicum.main_service.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventAdminService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;
    private final UserService userService;

    public List<EventDto> getEvents(List<Integer> usersIds, List<String> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {

        List<Event> events = eventRepository.findAll(PageRequest.of(from / size, size)).toList();

        Stream<Event> stream = events.stream();

        if (rangeStart != null && rangeEnd != null) {
            validateTimeRange(rangeStart, rangeEnd);
            stream = stream.filter(e -> e.getEventDate().isAfter(rangeStart) && e.getEventDate().isBefore(rangeEnd));
        }

        if (usersIds != null && !usersIds.isEmpty())
            stream = stream.filter(e -> usersIds.contains(e.getInitiator().getId()));

        if (states != null && !states.isEmpty())
            stream = stream.filter(e -> states.contains(e.getState()));

        if (categories != null && !categories.isEmpty())
            stream = stream.filter(e -> categories.contains(e.getCategory().getId()));

        return stream.map(eventMapper::toEventDto).toList();
    }

    public void validateTimeRange(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start))
            throw new IncorrectRequestException("Конец диапазона раньше начала");
        if (end.equals(start))
            throw new IncorrectRequestException("Начало и конец диапазона совпадают");
    }

    @Transactional
    public EventDto eventAdministration(int eventId, EventAdminDto dto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие с id " + eventId + " не найдено"));

        validateEventState(event);
        checkAdminTime(event.getEventDate());
        checkAdminTime(dto.getEventDate());
        applyStateChange(event, dto.getStateAction());
        applyEventUpdates(event, dto);

        return eventMapper.toEventDto(eventRepository.save(event));
    }

    private void validateEventState(Event event) {
        if (event.getState() == EventState.PUBLISHED)
            throw new ConflictRequestException("Событие уже опубликовано");
        if (event.getState() == EventState.CANCELED)
            throw new ConflictRequestException("Отменённые события нельзя публиковать");
    }

    private void applyEventUpdates(Event event, EventAdminDto dto) {
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getCategory() != 0) event.setCategory(userService.getCategoryById(dto.getCategory()));
        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());

        if (dto.getLocation() != null) {
            LocationDto loc = dto.getLocation();
            Location existing = locationRepository.getByLatAndLon(loc.getLat(), loc.getLon());
            event.setLocation(existing != null
                    ? existing
                    : locationRepository.save(locationMapper.fromLocationDto(loc)));
        }
    }

    private void applyStateChange(Event event, StateAdminAction action) {
        switch (action) {
            case REJECT_EVENT -> event.setState(EventState.CANCELED);
            case PUBLISH_EVENT -> {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            default -> throw new IncorrectRequestException("Некорректное действие администратора");
        }
    }

    private void checkAdminTime(LocalDateTime date) {
        if (date != null && date.isBefore(LocalDateTime.now().minusMinutes(60))) {
            throw new IncorrectRequestException("Дата события должна быть не ранее чем за час до публикации");
        }
    }
}
package ru.practicum.main_service.event.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.admin.service.EventAdminService;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSearchFilter;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.mapper.EventMapper;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.EventNotFoundException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventPublicService {

    private final EventRepository repository;
    private final EventMapper mapper;
    private final EventAdminService adminService;
    private final EventStatsService statsService;

    public EventDto getEventById(int id, HttpServletRequest request) {
        statsService.saveStats(request);

        Event event = repository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Событие с id " + id + " не найдено"));

        if (event.getState() != EventState.PUBLISHED) {

            throw new EventNotFoundException("Событие с id " + id + " не опубликовано");
        }

        int views = statsService.getViews(id);
        event.setViews(views);
        repository.updateViewsById(views, id);

        return mapper.toEventDto(event);
    }

    @Transactional
    public List<EventShortDto> getAllEvents(EventSearchFilter filter, HttpServletRequest request) {
        statsService.saveStats(request);

        List<Event> events = repository.findAll(PageRequest.of(filter.getFrom() / filter.getSize(),
                        filter.getSize()))
                .stream()
                .filter(e -> e.getState() == EventState.PUBLISHED)
                .collect(Collectors.toList());

        events = applyFilters(events, filter);

        events = sortEvents(events, filter.getSort());

        return events.stream()
                .map(mapper::toEventSmallDto)
                .collect(Collectors.toList());
    }

    private List<Event> applyFilters(List<Event> events, EventSearchFilter f) {
        return events.stream()
                .filter(e -> f.getText() == null ||
                        e.getAnnotation().toLowerCase().contains(f.getText().toLowerCase()) ||
                        e.getDescription().toLowerCase().contains(f.getText().toLowerCase()))
                .filter(e -> f.getCategories() == null || f.getCategories().contains(e.getCategory().getId()))
                .filter(e -> f.getPaid() == null || e.isPaid() == f.getPaid())
                .filter(e -> {
                    LocalDateTime start = f.getRangeStart() != null ? f.getRangeStart() : LocalDateTime.now();
                    LocalDateTime end = f.getRangeEnd();
                    if (end != null) adminService.validateTimeRange(start, end);
                    return e.getEventDate().isAfter(start) &&
                            (end == null || e.getEventDate().isBefore(end));
                })
                .filter(e -> !Boolean.TRUE.equals(f.getOnlyAvailable()) ||
                        e.getConfirmedRequests() < e.getParticipantLimit())
                .collect(Collectors.toList());
    }

    private List<Event> sortEvents(List<Event> events, String sort) {
        if (sort == null) return events;

        return switch (sort) {
            case "EVENT_DATE" -> events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
            case "VIEWS" -> events.stream()
                    .sorted(Comparator.comparingInt(Event::getViews).reversed())
                    .collect(Collectors.toList());
            default -> events;
        };
    }
}

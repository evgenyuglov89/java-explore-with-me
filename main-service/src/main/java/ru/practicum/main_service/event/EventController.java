package ru.practicum.main_service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventSearchFilter;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.service.EventPublicService;
import ru.practicum.main_service.exeption.IncorrectRequestException;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main_service.consts.Consts.DATE_FORMAT;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventPublicService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable int id, HttpServletRequest request) {
        return service.getEventById(id, request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeStart,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IncorrectRequestException("rangeStart не может быть позже rangeEnd");
        }

        EventSearchFilter filter = new EventSearchFilter(
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size
        );

        return service.getAllEvents(filter, request);
    }
}

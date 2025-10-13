package ru.practicum.main_service.event;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.client.StatsClient;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main_service.event.dto.EventDto;

@RestController
@RequestMapping(path = "/events")
public class EventController {
    final StatsClient statsClient;

    public EventController(StatsClient statsClient) {
        this.statsClient = statsClient;
    }

    @GetMapping
    private EventDto getEvents(HttpServletRequest request) {
        statsClient.postStats(request);
        return null;
    }

    @GetMapping("/{id}")
    private EventDto getEventById(@PathVariable int id, HttpServletRequest request) {
        statsClient.postStats(request);
        return null;
    }
}

package ru.practicum.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.exeptions.StatsNotFoundException;
import ru.practicum.server.service.StatsService;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.dto.EndpointHitDto.DATE_FORMAT;

@RestController
public class StatsController {

    private final StatsService service;

    @Autowired
    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@RequestBody EndpointHitDto stats) {
        if (stats.getTimestamp() == null) {
            stats.setTimestamp(LocalDateTime.now());
        }
        return service.create(stats);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(
            @RequestParam("start") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(pattern = DATE_FORMAT) LocalDateTime end,
            @RequestParam(value = "uris", required = false) List<String> uris,
            @RequestParam(value = "unique", defaultValue = "false") boolean unique
    ) {
        validateRequestParams(start, end);
        return service.getStats(start, end, uris, unique);
    }

    private void validateRequestParams(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new StatsNotFoundException("Некорректный диапазон времени: start должен быть раньше end");
        }
    }
}
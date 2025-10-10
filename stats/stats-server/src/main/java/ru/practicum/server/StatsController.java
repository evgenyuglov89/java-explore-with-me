package ru.practicum.server;

import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.exeptions.StatsNotFoundException;
import ru.practicum.server.service.StatsService;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/stats")
public class StatsController {

    final StatsService service;

    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto create(@RequestBody EndpointHitDto stats) {
        return service.create(stats);
    }

    @GetMapping
    public List<ViewStatsDto> getStats(@RequestParam("start") LocalDateTime start,
                                       @RequestParam("end") LocalDateTime end,
                                       @RequestParam(value = "uris", required = false) String[] uris,
                                       @RequestParam(value = "unique", defaultValue = "false") boolean unique) {

        validateRequestParams(start, end, uris);

        return service.getStats(start, end, uris, unique);
    }

    private void validateRequestParams(LocalDateTime start, LocalDateTime end, String[] uris) {
        if (start == null || end == null || start.isAfter(end)) {
            throw new StatsNotFoundException("Некорректный диапазон времени: start должен быть раньше end");
        }
        if (uris == null || uris.length == 0) {
            throw new StatsNotFoundException("Список URI не может быть пустым");
        }
    }
}
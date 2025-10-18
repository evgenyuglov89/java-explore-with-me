package ru.practicum.main_service.event.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventStatsService {

    private final StatsClient statsClient;

    public void saveStats(HttpServletRequest request) {
        try {
            statsClient.postStats(request);
        } catch (Exception e) {
            new Exception("Не удалось отправить статистику.");
        }
    }

    public int getViews(int eventId) {
        ResponseEntity<Object> response = statsClient.getStats(
                LocalDateTime.now().minusYears(2),
                LocalDateTime.now(),
                new String[]{"/events/" + eventId},
                true
        );

        if (response.getBody() == null) {
            return 0;
        }

        ObjectMapper mapper = new ObjectMapper();
        List<ViewStatsDto> stats = mapper.convertValue(response.getBody(), new TypeReference<List<ViewStatsDto>>() {});

        return stats.isEmpty() ? 0 : stats.get(0).getHits();
    }
}

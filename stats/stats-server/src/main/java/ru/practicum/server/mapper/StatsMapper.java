package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {

    public EndpointHitDto toStatisticsDto(Stats stat) {
        if (stat == null) return null;

        return EndpointHitDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }

    public ViewStatsDto toStatForListDto(Stats stat) {
        return ViewStatsDto
                .builder()
                .app(stat.getApp())
                .hits(stat.getHits())
                .uri(stat.getUri())
                .build();
    }

    public Stats fromStatisticsDto(EndpointHitDto dto) {
        return Stats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .hits(1)
                .build();
    }
}

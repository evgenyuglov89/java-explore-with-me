package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.Stats;

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

    public Stats fromStatisticsDto(EndpointHitDto statsDto) {
        return Stats
                .builder()
                .app(statsDto.getApp())
                .uri(statsDto.getUri())
                .timestamp(statsDto.getTimestamp())
                .ip(statsDto.getIp())
                .build();
    }
}

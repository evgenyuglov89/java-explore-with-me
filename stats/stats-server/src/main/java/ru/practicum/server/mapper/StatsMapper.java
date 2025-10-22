package ru.practicum.server.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
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

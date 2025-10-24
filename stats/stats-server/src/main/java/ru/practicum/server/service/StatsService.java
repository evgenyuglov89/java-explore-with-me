package ru.practicum.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.Stats;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StatsService {

    private final StatsMapper mapper;
    private final StatsRepository repository;

    public EndpointHitDto create(EndpointHitDto dto) {
        Stats stat = mapper.fromStatisticsDto(dto);

        repository.save(stat);

        return mapper.toStatisticsDto(stat);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (uris == null || uris.isEmpty()) {
            return repository.findAllWithoutUris(start, end);
        }
        if (unique) {
            return repository.findAllUnique(start, end, uris);
        }
        return repository.findAllNotUnique(start, end, uris);
    }
}

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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {

    private final StatsMapper mapper;
    private final StatsRepository repository;

    @Transactional
    public EndpointHitDto create(EndpointHitDto dto) {
        Stats stat = mapper.fromStatisticsDto(dto);

        Stats existing = repository.findByIpAndUriAndApp(stat.getIp(), stat.getUri(), stat.getApp())
                .orElse(null);

        if (existing != null) {
            existing.setHits(existing.getHits() + 1);
            existing.setTimestamp(LocalDateTime.now());
            repository.save(existing);
            return mapper.toStatisticsDto(existing);
        }

        stat.setHits(1);
        stat.setTimestamp(LocalDateTime.now());
        repository.save(stat);

        return mapper.toStatisticsDto(stat);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start,
                                       LocalDateTime end,
                                       String[] uris,
                                       boolean unique) {

        List<String> uriList = Arrays.asList(uris);

        List<Stats> stats = unique
                ? repository.findUniqueStatsBetweenDates(start, end, uriList)
                : repository.findStatsBetweenDates(start, end, uriList);

        return stats.stream()
                .map(mapper::toStatForListDto)
                .collect(Collectors.toList());
    }
}

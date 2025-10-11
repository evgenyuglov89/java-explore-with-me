package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    Optional<Stats> findByIpAndUriAndApp(String ip, String uri, String app);

    @Query("""
			SELECT new ru.practicum.dto.ViewStatsDto(s.app, s.uri, COUNT(s.ip))
            FROM Stats s
            WHERE s.timestamp between :start AND :end
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
			""")
    List<ViewStatsDto> findAllWithoutUris(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("""
			SELECT new ru.practicum.dto.ViewStatsDto(s.app, s.uri, COUNT(s.ip))
            FROM Stats s
            WHERE s.timestamp between :start AND :end
            AND s.uri in (:uris)
            GROUP BY s.app, s.uri
            ORDER BY COUNT(s.ip) DESC
			""")
    List<ViewStatsDto> findAllNotUnique(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);

    @Query("""
			SELECT new ru.practicum.dto.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip))
            FROM Stats s
            WHERE s.timestamp between :start AND :end
            AND s.uri in (:uris)
            GROUP BY s.app, s.uri
            ORDER BY COUNT(DISTINCT s.ip) DESC
			""")
    List<ViewStatsDto> findAllUnique(@Param("start") LocalDateTime start,
                                     @Param("end") LocalDateTime end,
                                     @Param("uris") List<String> uris);
}
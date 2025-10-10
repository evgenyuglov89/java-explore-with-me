package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatsRepository extends JpaRepository<Stats, Integer> {

    Optional<Stats> findByIpAndUriAndApp(String ip, String uri, String app);

    @Query("""
        SELECT s 
        FROM Stats s 
        WHERE s.timestamp BETWEEN :start AND :end 
          AND s.uri IN :uris
        """)
    List<Stats> findStatsBetweenDates(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("""
    SELECT s
    FROM Stats s
    WHERE s.timestamp BETWEEN :start AND :end
      AND (:uris IS NULL OR s.uri IN :uris)
    GROUP BY s.app, s.uri, s.ip, s.id, s.timestamp, s.hits
    """)
    List<Stats> findUniqueStatsBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}

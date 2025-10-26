package ru.practicum.main_service.event.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> getByInitiator(User initiator, PageRequest pageRequest);

    Event getByInitiatorAndId(User initiator, int id);

    @Modifying
    @Query("update Event e set e.views = :views where e.id = :id")
    @Transactional
    int updateViewsById(@Param("views") int views,
                        @Param("id") int eventId);

    boolean existsByCategoryId(int catId);

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.comments WHERE e.id = :id")
    Optional<Event> findByIdWithComments(@Param("id") int id);

    @Query("SELECT DISTINCT e FROM Event e LEFT JOIN FETCH e.comments")
    List<Event> findAllWithComments();
}

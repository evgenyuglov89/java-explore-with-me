package ru.practicum.main_service.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.model.RequestState;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByRequester_Id(int userId, PageRequest pageRequest);

    List<Request> getByEvent_Id(int eventId);

    int countByEvent_IdAndStatus(int eventId, RequestState status);

    boolean existsByRequester_IdAndEvent_Id(int userId, int eventId);

    Optional<Request> findByIdAndRequesterId(int requestId, int userId);
}

package ru.practicum.main_service.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.*;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.dto.RequestStatusUpdateRequestDto;
import ru.practicum.main_service.request.dto.RequestStatusUpdateResultDto;
import ru.practicum.main_service.request.mapper.RequestMapper;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.model.RequestState;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public List<RequestDto> getAllUserRequests(int userId, int from, int size) {
        User user = getUserById(userId);
        var requests = requestRepository.findByRequester_Id(user.getId(), PageRequest.of(from / size, size));
        return requests.stream().map(requestMapper::toRequestDto).collect(Collectors.toList());
    }

    public RequestDto addRequest(int userId, int eventId) {
        User requester = getUserById(userId);
        Event event = getEventById(eventId);

        validateRequestCreation(userId, event);

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(requester)
                .status(determineRequestState(event))
                .build();

        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    public RequestDto cancelRequest(int userId, int requestId) {
        Request request = getRequestById(requestId);
        User requester = getUserById(userId);

        if (!request.getRequester().equals(requester)) {
            throw new IncorrectRequestException("Пользователь не является владельцем запроса");
        }

        request.setStatus(RequestState.CANCELED);
        return requestMapper.toRequestDto(requestRepository.save(request));
    }

    public List<RequestDto> getUserRequestsByEvent(int userId, int eventId) {
        Event event = getEventById(eventId);
        User user = getUserById(userId);

        if (!Objects.equals(event.getInitiator().getId(), user.getId())) {
            throw new IncorrectRequestException("Пользователь не является организатором события");
        }

        return requestRepository.getByEvent_Id(eventId).stream()
                .map(requestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public RequestStatusUpdateResultDto changeRequestStatus(int userId, int eventId,
                                                            RequestStatusUpdateRequestDto request) {
        getUserById(userId);
        Event event = getEventById(eventId);

        validateConfirmationPossible(event);

        List<Request> requests = requestRepository.findAllById(request.getRequestIds());
        if (requests.size() != request.getRequestIds().size()) {
            throw new IncorrectRequestException("Некоторые запросы не найдены");
        }

        int confirmed = requestRepository.countByEvent_IdAndStatus(eventId, RequestState.CONFIRMED);
        int limit = event.getParticipantLimit();

        if (limit > 0 && confirmed >= limit) {
            throw new ConflictRequestException("Лимит участников достигнут");
        }

        RequestStatusUpdateResultDto result = new RequestStatusUpdateResultDto();

        for (Request r : requests) {
            if (!r.getStatus().equals(RequestState.PENDING)) {
                throw new ConflictRequestException("Заявка должна быть в статусе PENDING");
            }

            if (request.getStatus().equals(RequestState.CONFIRMED)) {
                r.setStatus(RequestState.CONFIRMED);
                result.getConfirmedRequests().add(requestMapper.toRequestDto(r));
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                eventRepository.save(event);
            } else {
                r.setStatus(RequestState.REJECTED);
                result.getRejectedRequests().add(requestMapper.toRequestDto(r));
            }
        }

        requestRepository.saveAll(requests);
        return result;
    }

    private void validateRequestCreation(int userId, Event event) {
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictRequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (userId == event.getInitiator().getId()) {
            throw new ConflictRequestException("Организатор не может участвовать в своём событии");
        }
        if (requestRepository.existsByRequester_IdAndEvent_Id(userId, event.getId())) {
            throw new ConflictRequestException("Запрос уже существует");
        }

        int confirmedRequests = requestRepository.countByEvent_IdAndStatus(event.getId(), RequestState.CONFIRMED);
        int participantLimit = event.getParticipantLimit();

        if (participantLimit > 0 && confirmedRequests >= participantLimit) {
            throw new ConflictRequestException("Лимит участников достигнут");
        }
    }

    private RequestState determineRequestState(Event event) {
        if (event.getParticipantLimit() == 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);

            return RequestState.CONFIRMED;
        }

        return event.isRequestModeration() ? RequestState.PENDING : RequestState.CONFIRMED;
    }

    private void validateConfirmationPossible(Event event) {
        if (event.getParticipantLimit() == 0 && !event.isRequestModeration()) {
            throw new ConflictRequestException("Подтверждение заявок не требуется");
        }
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие с id " + eventId + " не найдено"));
    }

    private Request getRequestById(int requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new RequestNotFoundException("Запрос с id " + requestId + " не найден"));
    }
}


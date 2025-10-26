package ru.practicum.main_service.comment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.mapper.CommentMapper;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.comment.repository.CommentRepository;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.event.model.EventState;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.*;
import ru.practicum.main_service.request.model.Request;
import ru.practicum.main_service.request.model.RequestState;
import ru.practicum.main_service.request.repository.RequestRepository;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional()
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CommentMapper mapper;

    public List<CommentDto> getComments(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return mapper.toDtoList(commentRepository.findAll(pageable).getContent());
    }

    public List<CommentDto> getAllCommentsByEventId(int eventId, int from, int size) {
        ensureEventExists(eventId);
        PageRequest pageRequest = PageRequest.of(from / size, size);
        return mapper.toDtoList(commentRepository.findCommentsByEventIdOrderByCreatedDesc(
                eventId, pageRequest).getContent());
    }

    public CommentDto findCommentById(int commentId) {
        Comment comment = getCommentById(commentId);
        return mapper.toCommentDto(comment);
    }

    public CommentDto createComment(int userId, int eventId, CommentDto commentDto) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);

        validateEventIsPublished(event);
        validateUserParticipation(user, event);
        ensureUserHasNoComment(userId, eventId);

        Comment comment = mapper.fromCommentDto(commentDto, userId, eventId);
        return mapper.toCommentDto(commentRepository.save(comment));
    }

    public void deleteCommentById(int commentId, int userId) {
        ensureUserExists(userId);
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(comment.getAuthor().getId(), userId, commentId);

        commentRepository.delete(comment);
    }

    public CommentDto updateCommentById(int commentId, int userId, CommentDto commentDto) {
        Comment comment = getCommentById(commentId);
        validateCommentAuthor(comment.getAuthor().getId(), userId, commentId);

        if (StringUtils.hasText(commentDto.getText())) {
            comment.setText(commentDto.getText());
        }

        return mapper.toCommentDto(commentRepository.save(comment));
    }

    private void validateEventIsPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictRequestException("Статус события должен быть 'PUBLISHED'");
        }
    }

    private void validateUserParticipation(User user, Event event) {
        boolean isInitiator = user.getId() == event.getInitiator().getId();
        Optional<Request> request = requestRepository.findByIdAndRequesterId(user.getId(), event.getId());

        boolean participated = request.isPresent() && request.get().getStatus() == RequestState.CONFIRMED;

        if (!isInitiator && !participated) {
            throw new IncorrectRequestException("Пользователь с id=" + user.getId()
                    + " не участвовал в событии с id=" + event.getId() + " и не может оставить комментарий");
        }
    }

    private void ensureUserHasNoComment(int userId, int eventId) {
        if (commentRepository.findCommentByEventIdAndAuthorId(eventId, userId).isPresent()) {
            throw new IncorrectRequestException("Пользователь с id=" + userId +
                    " уже оставлял комментарий к событию с id=" + eventId);
        }
    }

    private void validateCommentAuthor(int authorId, int userId, int commentId) {
        if (authorId != userId) {
            throw new IncorrectRequestException("Пользователь с id=" + userId
                    + " не является автором комментария с id=" + commentId);
        }
    }

    private void ensureEventExists(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Событие с id " + eventId + " не найдено");
        }
    }

    private void ensureUserExists(int userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь с id=" + userId + " не найден");
        }
    }

    private Comment getCommentById(int commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий с id=" + commentId + " не найден"));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Событие с id=" + eventId + " не найден"));
    }
}

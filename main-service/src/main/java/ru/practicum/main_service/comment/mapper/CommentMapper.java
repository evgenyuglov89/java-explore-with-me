package ru.practicum.main_service.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        if (comments == null || comments.isEmpty()) {
            return Collections.emptyList();
        }
        return comments.stream()
                .map(this::toCommentDto)
                .collect(Collectors.toList());
    }

    public Comment fromCommentDto(CommentDto dto, int userId, int eventId) {
        return Comment.builder()
                .text(dto.getText())
                .author(User.builder().id(userId).build())
                .event(Event.builder().id(eventId).build())
                .created(LocalDateTime.now())
                .build();
    }
}

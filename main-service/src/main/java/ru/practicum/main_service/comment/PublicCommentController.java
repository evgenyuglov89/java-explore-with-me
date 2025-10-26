package ru.practicum.main_service.comment;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class PublicCommentController {
    private final CommentService service;

    @GetMapping
    public List<CommentDto> getAllComments(
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getComments(from, size);
    }

    @GetMapping("/events/{eventId}")
    public List<CommentDto> getCommentsByEvent(
            @PathVariable int eventId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        return service.getAllCommentsByEventId(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable int commentId) {
        return service.findCommentById(commentId);
    }
}

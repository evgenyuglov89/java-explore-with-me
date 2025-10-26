package ru.practicum.main_service.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.comment.dto.CommentDto;
import ru.practicum.main_service.comment.service.CommentService;

@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class UserCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable int userId,
                                    @RequestParam int eventId,
                                    @Valid @RequestBody CommentDto requestDto) {
        return service.createComment(userId, eventId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable int userId,
                              @PathVariable int commentId) {
        service.deleteCommentById(commentId, userId);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable int userId,
                                    @PathVariable int commentId,
                                    @Valid @RequestBody CommentDto requestDto) {
        return service.updateCommentById(commentId, userId, requestDto);
    }
}

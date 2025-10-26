package ru.practicum.main_service.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_service.comment.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Page<Comment> findCommentsByEventIdOrderByCreatedDesc(int eventId, Pageable pageable);

    Optional<Comment> findCommentByEventIdAndAuthorId(int eventId, int authorId);
}

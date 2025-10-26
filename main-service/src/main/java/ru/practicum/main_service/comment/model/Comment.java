package ru.practicum.main_service.comment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 3, max = 1000)
    private String text;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private LocalDateTime created;
}

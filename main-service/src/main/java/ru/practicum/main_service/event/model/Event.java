package ru.practicum.main_service.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.comment.model.Comment;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String annotation;

    @NotEmpty
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @NotNull
    @Column(name = "paid")
    private boolean paid;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    private int views;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @OneToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @NotNull
    @Column(name = "request_moderation")
    private boolean requestModeration;

    private int confirmedRequests;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}

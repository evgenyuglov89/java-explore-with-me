package ru.practicum.main_service.event.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.location.model.Location;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String annotation;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

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

    @Column(name = "request_moderation")
    private boolean requestModeration;

    private int confirmedRequests;

}

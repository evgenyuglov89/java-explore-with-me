package ru.practicum.main_service.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.main_service.event.model.Event;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(name = "created")
    private LocalDateTime created;

    @Enumerated(EnumType.STRING)
    private RequestState status;
}
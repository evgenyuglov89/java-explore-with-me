package ru.practicum.server.model;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Stats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String app;

    private String uri;

    private String ip;

    @Column(name = "time")
    private LocalDateTime timestamp;

    private int hits;
}

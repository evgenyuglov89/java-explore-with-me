package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String app;

    private String uri;

    private String ip;

    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime timestamp;
}

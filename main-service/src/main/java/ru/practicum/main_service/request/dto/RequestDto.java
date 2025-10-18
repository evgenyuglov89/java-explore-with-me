package ru.practicum.main_service.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.request.model.RequestState;

import java.time.LocalDateTime;

import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.TIME_FORMAT;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private int id;

    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime created;

    private int event;

    private int requester;

    private RequestState status;
}

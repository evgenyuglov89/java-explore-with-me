package ru.practicum.main_service.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.model.Request;

@Component
public class RequestMapper {
    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }

    public Request fromRequestDto(RequestDto dto) {
        return Request.builder()
                .status(dto.getStatus())
                .created(dto.getCreated())
                .build();
    }
}

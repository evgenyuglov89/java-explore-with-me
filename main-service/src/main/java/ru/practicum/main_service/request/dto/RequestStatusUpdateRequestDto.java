package ru.practicum.main_service.request.dto;

import lombok.*;
import ru.practicum.main_service.request.model.RequestState;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestStatusUpdateRequestDto {
    private RequestState status;

    private List<Integer> requestIds;
}

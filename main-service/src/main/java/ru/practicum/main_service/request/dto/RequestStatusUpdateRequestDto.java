package ru.practicum.main_service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.request.model.RequestState;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestStatusUpdateRequestDto {
    private RequestState status;

    private List<Integer> requestIds;
}

package ru.practicum.main_service.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class RequestStatusUpdateResultDto {

    private final List<RequestDto> confirmedRequests;
    private final List<RequestDto> rejectedRequests;

    public RequestStatusUpdateResultDto() {
        this.confirmedRequests = new ArrayList<>();
        this.rejectedRequests = new ArrayList<>();
    }

}
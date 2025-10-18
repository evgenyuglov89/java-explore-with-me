package ru.practicum.main_service.location.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class LocationDto {
    private Double lat;

    private Double lon;
}

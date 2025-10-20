package ru.practicum.main_service.location.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class LocationDto {
    @NotNull
    private Double lat;

    @NotNull
    private Double lon;
}

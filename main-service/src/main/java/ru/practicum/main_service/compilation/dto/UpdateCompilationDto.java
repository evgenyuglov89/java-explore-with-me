package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDto {
    private String title;

    private Boolean pinned;

    private List<Integer> events;
}

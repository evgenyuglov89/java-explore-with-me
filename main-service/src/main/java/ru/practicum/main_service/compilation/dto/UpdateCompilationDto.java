package ru.practicum.main_service.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String title;

    private Boolean pinned;

    private List<Integer> events;
}

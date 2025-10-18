package ru.practicum.main_service.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.main_service.event.dto.EventShortDto;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    private int id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;
}
package ru.practicum.main_service.compilation.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.event.mapper.EventMapper;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public Compilation fromCompilationNewDto(UpdateCompilationDto dto) {
        Boolean pinned = (dto.getPinned() != null) ? dto.getPinned() : false;

        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(pinned)
                .build();
    }

    public CompilationDto toCompilationBigDto(Compilation compilation) {
        CompilationDto dto = CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.isPinned())
                .build();
        if (compilation.getEvents() != null) {
            dto.setEvents(compilation
                    .getEvents().stream()
                    .map(eventMapper::toEventSmallDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}

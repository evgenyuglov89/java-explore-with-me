package ru.practicum.main_service.compilation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.mapper.CompilationMapper;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.exeption.CompilationNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;

    public List<CompilationDto> getAllCompilations(boolean pinned, int from, int size) {
        PageRequest pageRequest = PageRequest.of(from / size, size);
        List<Compilation> compilations;

        if (pinned) {
            compilations = compilationRepository.findAllByPinned(true, pageRequest);
        } else {
            compilations = compilationRepository.findAll(pageRequest).getContent();
        }

        return compilations.stream()
                .map(compilationMapper::toCompilationBigDto)
                .collect(Collectors.toList());
    }

    public CompilationDto getCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка с id " + compId + " не найдена"));

        return compilationMapper.toCompilationBigDto(compilation);
    }
}


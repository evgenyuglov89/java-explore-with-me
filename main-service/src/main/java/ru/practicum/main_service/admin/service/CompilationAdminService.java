package ru.practicum.main_service.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.compilation.mapper.CompilationMapper;
import ru.practicum.main_service.compilation.model.Compilation;
import ru.practicum.main_service.compilation.repository.CompilationRepository;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.CompilationNotFoundException;
import ru.practicum.main_service.exeption.ConflictRequestException;

@Service
@RequiredArgsConstructor
public class CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    @Transactional
    public CompilationDto addCompilation(UpdateCompilationDto dto) {
        validateTitle(dto.getTitle());
        Compilation compilation = compilationMapper.fromCompilationNewDto(dto);

        if (dto.getEvents() != null)
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));

        return compilationMapper.toCompilationBigDto(compilationRepository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(int id) {
        if (!compilationRepository.existsById(id)) {
            throw new CompilationNotFoundException("Подборка с id " + id + " не найдена");
        }
        compilationRepository.deleteById(id);
    }

    @Transactional
    public CompilationDto updateCompilation(int id, UpdateCompilationDto dto) {
        validateTitle(dto.getTitle());
        Compilation compilation = getCompilation(id);

        compilation.setTitle(dto.getTitle());
        if (dto.getPinned() != null) compilation.setPinned(dto.getPinned());
        if (dto.getEvents() != null)
            compilation.setEvents(eventRepository.findAllById(dto.getEvents()));

        return compilationMapper.toCompilationBigDto(compilationRepository.save(compilation));
    }

    private Compilation getCompilation(int id) {
        return compilationRepository.findById(id)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка с id " + id + " не найдена"));
    }

    private void validateTitle(String title) {
        if (compilationRepository.existsByTitle(title)) {
            throw new ConflictRequestException("Компиляция с названием " + title + " уже существует");
        }
    }
}
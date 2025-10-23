package ru.practicum.main_service.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.event.repository.EventRepository;
import ru.practicum.main_service.exeption.CategoryNotFoundException;
import ru.practicum.main_service.exeption.ConflictRequestException;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    public Category addCategory(CategoryDto dto) {
        if (categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ConflictRequestException("Имя категории должно быть уникальным");
        }

        Category category = categoryMapper.fromCategoryDto(dto);
        return categoryRepository.save(category);
    }

    public void deleteCategory(int id) {
        if (eventRepository.existsByCategoryId(id)) {
            throw new ConflictRequestException("Нельзя удалить категорию, к которой привязаны события");
        }

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id " + id + " не найдена"));

        categoryRepository.delete(category);
    }

    public Category editCategory(int id, CategoryDto dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id " + id + " не найдена"));

        if (!existing.getName().equals(dto.getName()) &&
                categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new ConflictRequestException("Категория с таким именем уже существует");
        }

        existing.setName(dto.getName());
        return categoryRepository.save(existing);
    }
}

package ru.practicum.main_service.category.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.category.repository.CategoryRepository;
import ru.practicum.main_service.exeption.CategoryNotFoundException;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository repository;

    private final CategoryMapper mapper;

    public CategoryService(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<Category> getCategories(int from, int size) {
        Page<Category> categories = repository.findAll(PageRequest.of(from / size, size));
        return categories.getContent();
    }


    public Category getCategoriesById(int id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Категория с id " + id + " не найдена"));
    }
}

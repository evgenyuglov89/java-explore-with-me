package ru.practicum.main_service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.mapper.CategoryMapper;
import ru.practicum.main_service.category.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/categories")
public class CategoryController {
    private final CategoryService service;
    private final CategoryMapper mapper;

    @ResponseBody
    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        return service.getCategories(from, size).stream().map(mapper::toCategoryDto).collect(Collectors.toList());
    }

    @ResponseBody
    @GetMapping(path = "/{id}")
    public CategoryDto getCategoryById(@PathVariable int id) {
        return mapper.toCategoryDto(service.getCategoriesById(id));
    }
}
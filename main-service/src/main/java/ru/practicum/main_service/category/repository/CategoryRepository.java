package ru.practicum.main_service.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main_service.category.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("SELECT c.name FROM Category c")
    List<String> findAllNames();

    boolean existsByNameIgnoreCase(String name);
}
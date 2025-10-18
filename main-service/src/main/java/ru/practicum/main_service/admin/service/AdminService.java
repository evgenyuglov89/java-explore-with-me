package ru.practicum.main_service.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.category.dto.CategoryDto;
import ru.practicum.main_service.category.model.Category;
import ru.practicum.main_service.compilation.dto.CompilationDto;
import ru.practicum.main_service.compilation.dto.UpdateCompilationDto;
import ru.practicum.main_service.event.dto.EventAdminDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final CategoryAdminService categoryAdminService;
    private final UserAdminService userAdminService;
    private final EventAdminService eventAdminService;
    private final CompilationAdminService compilationAdminService;

    public Category addCategory(CategoryDto dto) {
        return categoryAdminService.addCategory(dto);
    }

    public void deleteCategory(int id) {
        categoryAdminService.deleteCategory(id);
    }

    public Category editCategory(int id, CategoryDto dto) {
        return categoryAdminService.editCategory(id, dto);
    }

    public List<User> getUsers(List<Integer> ids, int from, int size) {
        return userAdminService.getUsers(ids, from, size);
    }

    public User addUser(UserDto dto) {
        return userAdminService.addUser(dto);
    }

    public void deleteUser(int id) {
        userAdminService.deleteUser(id);
    }

    public List<EventDto> getEvents(List<Integer> usersIds, List<String> states, List<Integer> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return eventAdminService.getEvents(usersIds, states, categories, rangeStart, rangeEnd, from, size);
    }

    public EventDto eventAdministration(int eventId, EventAdminDto dto) {
        return eventAdminService.eventAdministration(eventId, dto);
    }

    public void deleteCompilation(int compId) {
        compilationAdminService.deleteCompilation(compId);
    }

    public CompilationDto addCompilation(UpdateCompilationDto dto) {
        return compilationAdminService.addCompilation(dto);
    }

    public CompilationDto updateCompilation(int compId, UpdateCompilationDto dto) {
        return compilationAdminService.updateCompilation(compId, dto);
    }
}

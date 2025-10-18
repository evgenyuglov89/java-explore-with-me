package ru.practicum.main_service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.admin.service.AdminService;
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

import static org.hibernate.type.descriptor.java.JdbcTimeJavaType.TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category addCategory(@RequestBody CategoryDto categoryDto) {
        return service.addCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable int catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public Category editCategory(@PathVariable int catId,
                                  @RequestBody CategoryDto categoryDto) {
        return service.editCategory(catId, categoryDto);
    }

    @GetMapping("/users")
    public List<User> getUsers(@RequestParam(required = false) List<Integer> ids,
                               @RequestParam(defaultValue = "0") int from,
                               @RequestParam(defaultValue = "10") int size) {
        return service.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser( @RequestBody UserDto userDto) {
        return service.addUser(userDto);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }

    @GetMapping("/events")
    public List<EventDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                    @RequestParam(required = false) List<String> states,
                                    @RequestParam(required = false) List<Integer> categories,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime rangeStart,
                                    @RequestParam(required = false)
                                    @DateTimeFormat(pattern = TIME_FORMAT) LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        return service.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventDto updateEventByAdmin(@PathVariable int eventId,
                                       @RequestBody EventAdminDto dto) {
        return service.eventAdministration(eventId, dto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable int compId) {
        service.deleteCompilation(compId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody UpdateCompilationDto dto) {
        return service.addCompilation(dto);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId,
                                               @RequestBody UpdateCompilationDto dto) {
        return service.updateCompilation(compId, dto);
    }
}

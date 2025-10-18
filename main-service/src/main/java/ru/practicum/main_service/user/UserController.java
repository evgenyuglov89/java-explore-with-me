package ru.practicum.main_service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.event.dto.EventCreateDto;
import ru.practicum.main_service.event.dto.EventDto;
import ru.practicum.main_service.event.dto.EventShortDto;
import ru.practicum.main_service.event.dto.EventUpdateDto;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.dto.RequestStatusUpdateRequestDto;
import ru.practicum.main_service.request.dto.RequestStatusUpdateResultDto;
import ru.practicum.main_service.request.service.RequestService;
import ru.practicum.main_service.user.service.UserService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class UserController {

    private final UserService userService;
    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@PathVariable int userId, @RequestBody EventCreateDto dto) {
        return userService.createEvent(userId, dto);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable int userId,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return userService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventDto getEventByUser(@PathVariable int userId, @PathVariable int eventId) {
        return userService.getEventByInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventDto updateEvent(@PathVariable int userId,
                                @PathVariable int eventId,
                                @RequestBody EventUpdateDto dto) {
        return userService.changeEvent(userId, eventId, dto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestsByEvent(@PathVariable int userId, @PathVariable int eventId) {
        return requestService.getUserRequestsByEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateResultDto changeRequestStatus(@PathVariable int userId,
                                                            @PathVariable int eventId,
                                                            @RequestBody RequestStatusUpdateRequestDto request) {
        return requestService.changeRequestStatus(userId, eventId, request);
    }
}


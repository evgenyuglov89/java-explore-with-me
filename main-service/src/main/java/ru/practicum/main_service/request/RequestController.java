package ru.practicum.main_service.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.request.dto.RequestDto;
import ru.practicum.main_service.request.service.RequestService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto addRequest(@PathVariable int userId, @RequestParam int eventId) {
        return requestService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        return requestService.cancelRequest(userId, requestId);
    }

    @GetMapping
    public List<RequestDto> getUserRequests(@PathVariable int userId,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size) {
        return requestService.getAllUserRequests(userId, from, size);
    }
}


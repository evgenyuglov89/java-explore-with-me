package ru.practicum.main_service.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.model.User;

@Component
public class UserMapper {
    public User fromUserDto(UserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .build();
    }

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}
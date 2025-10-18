package ru.practicum.main_service.admin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main_service.exeption.ConflictRequestException;
import ru.practicum.main_service.exeption.UserNotFoundException;
import ru.practicum.main_service.user.dto.UserDto;
import ru.practicum.main_service.user.mapper.UserMapper;
import ru.practicum.main_service.user.model.User;
import ru.practicum.main_service.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> getUsers(List<Integer> ids, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return (ids != null && !ids.isEmpty())
                ? userRepository.findByIdIn(ids, pageable).getContent()
                : userRepository.findAll(pageable).getContent();
    }

    @Transactional
    public User addUser(UserDto dto) {
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new ConflictRequestException("Пользователь с таким email уже существует");
        }
        return userRepository.save(userMapper.fromUserDto(dto));
    }

    @Transactional
    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Пользователь с id " + id + " не найден");
        }
        userRepository.deleteById(id);
    }
}

package ru.plus.irbis.web.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.plus.irbis.web.app.model.entity.User;
import ru.plus.irbis.web.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    public Optional<User> findById(Integer id) {
        log.info("Fetching user by id {}", id);
        return userRepository.findById(id);
    }
}

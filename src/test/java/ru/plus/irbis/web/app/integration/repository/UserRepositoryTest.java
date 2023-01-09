package ru.plus.irbis.web.app.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.integration.annotation.IT;
import ru.plus.irbis.web.app.model.entity.Role;
import ru.plus.irbis.web.app.model.entity.User;
import ru.plus.irbis.web.app.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class UserRepositoryTest extends IntegrationTestBase {

    private final UserRepository userRepository;

    @Test
    void findByUsername() {
        Optional<User> admin = userRepository.findByUsername("admin");
        assertTrue(admin.isPresent());
        admin.ifPresent(user -> assertEquals(1, user.getId()));
        admin.ifPresent(user -> assertEquals(Role.ADMIN, user.getRole()));
    }

    @Test
    void getAll() {
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void findById() {
        Optional<User> user = userRepository.findById(2);
        assertTrue(user.isPresent());
        user.ifPresent(u -> assertEquals(2, u.getId()));
        user.ifPresent(u -> assertEquals(Role.USER, u.getRole()));
    }
}

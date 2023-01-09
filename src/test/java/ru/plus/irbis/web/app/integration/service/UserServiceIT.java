package ru.plus.irbis.web.app.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.integration.annotation.IT;
import ru.plus.irbis.web.app.model.entity.Role;
import ru.plus.irbis.web.app.model.entity.User;
import ru.plus.irbis.web.app.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IT
@RequiredArgsConstructor
public class UserServiceIT extends IntegrationTestBase {

    private static final Integer USER_ID = 1;

    private final UserService userService;

    @Test
    void findById() {
        Optional<User> actualResult = userService.findById(USER_ID);

        assertTrue(actualResult.isPresent());

        User expectedResult = User.builder()
                .id(USER_ID)
                .username("admin")
                .password("$2a$10$10GqyO9ZndGEVx9wSxgN0.KdChIpZjewtbf.VLHYiE2CBctzSlJva")
                .role(Role.ADMIN)
                .build();
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }

    @Test
    void getAll() {
        List<User> users = userService.getAll();
        assertThat(users).hasSize(2);
    }
}

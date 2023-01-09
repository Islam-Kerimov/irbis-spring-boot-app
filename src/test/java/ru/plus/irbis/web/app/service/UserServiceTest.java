package ru.plus.irbis.web.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.plus.irbis.web.app.model.entity.User;
import ru.plus.irbis.web.app.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final Integer USER_ID = 1;

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getAll() {
    }

    @Test
    void findById() {
        Mockito.doReturn(Optional.of(User.builder().id(USER_ID).build()))
                .when(userRepository).findById(USER_ID);

        Optional<User> actualResult = userService.findById(USER_ID);

        assertTrue(actualResult.isPresent());

        User expectedResult = User.builder().id(USER_ID).build();
        actualResult.ifPresent(actual -> assertEquals(expectedResult, actual));
    }
}
package ru.plus.irbis.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plus.irbis.web.app.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

}

package ru.plus.irbis.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plus.irbis.web.app.model.entity.NewsSource;

import java.util.Optional;

public interface NewsSourceRepository extends JpaRepository<NewsSource, Integer> {

    Optional<NewsSource> findByName(String name);
}

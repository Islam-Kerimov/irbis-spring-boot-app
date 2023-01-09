package ru.plus.irbis.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plus.irbis.web.app.model.entity.NewsTopic;

public interface NewsTopicRepository extends JpaRepository<NewsTopic, Integer> {

    NewsTopic findByNameAndSourceId(String name, Integer id);
}

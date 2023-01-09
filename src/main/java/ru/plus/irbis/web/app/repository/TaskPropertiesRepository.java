package ru.plus.irbis.web.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.plus.irbis.web.app.model.entity.TaskProperties;

public interface TaskPropertiesRepository extends JpaRepository<TaskProperties, Integer> {
}

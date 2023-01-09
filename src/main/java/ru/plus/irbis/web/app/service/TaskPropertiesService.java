package ru.plus.irbis.web.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.plus.irbis.web.app.model.entity.TaskProperties;
import ru.plus.irbis.web.app.repository.TaskPropertiesRepository;

import java.util.List;

/**
 * Сервис отвечающий за получения данных планирования задач из базы.
 */
@Service
@Slf4j
public class TaskPropertiesService {

    private final TaskPropertiesRepository taskPropertiesRepository;

    public TaskPropertiesService(TaskPropertiesRepository taskPropertiesRepository) {
        this.taskPropertiesRepository = taskPropertiesRepository;
    }

    public List<TaskProperties> getTaskProperties() {
        log.info("Fetching all task properties");
        return taskPropertiesRepository.findAll();
    }
}

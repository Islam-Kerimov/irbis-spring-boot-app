package ru.plus.irbis.web.app.task;

import ru.plus.irbis.web.app.model.entity.TaskProperties;
import ru.plus.irbis.web.app.service.ScheduledTaskService;

/**
 * Задача добавления данных новостей в базу.
 */
public class ScheduledTaskNews implements Runnable {
    private final ScheduledTaskService scheduledTaskService;

    private final TaskProperties taskProperties;

    public ScheduledTaskNews(TaskProperties taskProperties,
                             ScheduledTaskService scheduledTaskService) {
        this.taskProperties = taskProperties;
        this.scheduledTaskService = scheduledTaskService;
    }

    @Override
    public void run() {
        process();
    }

    protected void process() {
        String options = taskProperties.getOptions();
        scheduledTaskService.writeToCsv(options);
    }
}

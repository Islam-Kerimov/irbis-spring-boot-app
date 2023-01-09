package ru.plus.irbis.web.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;

import java.util.Optional;

import static ru.plus.irbis.web.app.service.utils.CsvWriter.write;

@Service
@Slf4j
public class ScheduledTaskService {

    private final NewsSourceRepository newsSourceRepository;

    public ScheduledTaskService(NewsSourceRepository newsSourceRepository) {
        this.newsSourceRepository = newsSourceRepository;
    }

    public void writeToCsv(String options) {
        Optional<NewsSource> source = newsSourceRepository.findByName(options);
        log.info("Fetching task options");
        source.ifPresent(newsSource -> write(newsSource.getTopics(), options));
    }
}

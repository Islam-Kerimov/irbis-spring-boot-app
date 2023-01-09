package ru.plus.irbis.web.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.repository.NewsBodyRepository;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;
import ru.plus.irbis.web.app.repository.NewsTopicRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@Transactional(readOnly = true)
@Slf4j
public class NewsService {

    private final NewsSourceRepository newsSourceRepository;

    private final NewsTopicRepository newsTopicRepository;

    private final NewsBodyRepository newsBodyRepository;

    public NewsService(NewsSourceRepository newsSourceRepository,
                       NewsTopicRepository newsTopicRepository,
                       NewsBodyRepository newsBodyRepository) {
        this.newsSourceRepository = newsSourceRepository;
        this.newsTopicRepository = newsTopicRepository;
        this.newsBodyRepository = newsBodyRepository;
    }

    public List<NewsSource> getAllSources() {
        List<NewsSource> sources = newsSourceRepository.findAll(Sort.by("id"));
        log.info("Fetching all sources, found - {} source", sources.size());
        return sources;
    }

    public Optional<NewsSource> findSourceById(Integer id) {
        log.info("Fetching source by id - {}", id);
        return newsSourceRepository.findById(id);
    }

    @Transactional
    public NewsSource createSource(NewsSource source) {
        log.info("Saving new source {} to Database", source);
        return newsSourceRepository.save(source);
    }

    @Transactional
    public Optional<NewsSource> updateSource(Integer id, NewsSource source) {
        return newsSourceRepository.findById(id)
                .map(entity -> {
                    source.setId(id);
                    log.info("Updating source {} by id {} to Database", source, id);
                    return newsSourceRepository.saveAndFlush(source);
                });
    }

    @Transactional
    public boolean deleteSource(Integer id) {
        return newsSourceRepository.findById(id)
                .map(entity -> {
                    log.info("Deleting source by id {} from Database", id);
                    newsSourceRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }

    public List<NewsTopic> getAllTopics() {
        log.info("Fetching all topics sorting by id");
        return newsTopicRepository.findAll(Sort.by("id"));
    }

    public Optional<NewsTopic> findTopicById(Integer id) {
        log.info("Fetching topic by id - {}", id);
        return newsTopicRepository.findById(id);
    }

    @Transactional
    public NewsTopic createTopic(NewsTopic topic) {
        Optional<NewsSource> source = findSourceById(topic.getSourceId());
        topic.setNewsSource(source.orElse(null));
        log.info("Saving new topic {} to Database", topic);
        return newsTopicRepository.save(topic);
    }

    @Transactional
    public Optional<NewsTopic> updateTopic(Integer id, NewsTopic topic) {
        return newsTopicRepository.findById(id)
                .map(entity -> {
                    Optional<NewsSource> source = newsSourceRepository.findById(topic.getSourceId());
                    topic.setNewsSource(source.orElse(null));
                    topic.setId(id);
                    log.info("Updating topic {} by id {} to Database", topic, id);
                    return newsTopicRepository.saveAndFlush(topic);
                });
    }

    @Transactional
    public boolean deleteTopic(Integer id) {
        return newsTopicRepository.findById(id)
                .map(entity -> {
                    log.info("Deleting topic by id {} from Database", id);
                    newsTopicRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }

    public Page<NewsBody> getAllNews(Pageable pageable) {
        log.info("Fetching all news");
        return newsBodyRepository.findAll(pageable);
    }

    public Page<NewsBody> getNewsBySource(String source, Pageable pageable) {
        log.info("Fetching news in page - {}, size - {} by source name - {}",
                pageable.getPageNumber(), pageable.getPageSize(), source);
        return newsBodyRepository.findBySource(source, pageable);
    }

    public Page<NewsBody> getNewsByTopic(Integer id, Pageable pageable) {
        log.info("Fetching news in page - {}, size - {} by topic id - {}",
                pageable.getPageNumber(), pageable.getPageSize(), id);
        return newsBodyRepository.findByTopicId(id, pageable);
    }

    @Transactional
    public Optional<NewsBody> createNews(NewsBody newsBody, String topicName, String sourceName) {
        Optional<NewsSource> source = newsSourceRepository.findByName(sourceName);
        if (source.isPresent()) {
            NewsTopic topic = newsTopicRepository.findByNameAndSourceId(topicName, source.get().getId());
            if (topic != null) {
                newsBody.setNewsTopic(topic);
                log.info("Saving new news {} to Database", newsBody);
                return of(newsBodyRepository.save(newsBody));
            }
        }
        return empty();
    }

    @Transactional
    public Optional<NewsBody> updateNewsBody(Integer id, NewsBody newsBody, String topicName, String sourceName) {
        return newsBodyRepository.findById(id)
                .map(entity -> {
                    Optional<NewsSource> source = newsSourceRepository.findByName(sourceName);
                    if (source.isPresent()) {
                        NewsTopic topic = newsTopicRepository.findByNameAndSourceId(topicName, source.get().getId());
                        if (topic != null) {
                            newsBody.setId(id);
                            newsBody.setNewsTopic(topic);
                            log.info("Updating news {} by id {} to Database", newsBody, id);
                            return newsBodyRepository.saveAndFlush(newsBody);
                        }
                    }
                    return null;
                });
    }

    @Transactional
    public boolean deleteNewsBody(Integer id) {
        return newsBodyRepository.findById(id)
                .map(entity -> {
                    log.info("Deleting news by id {} from Database", id);
                    newsBodyRepository.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}

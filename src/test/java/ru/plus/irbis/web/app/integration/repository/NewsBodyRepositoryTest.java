package ru.plus.irbis.web.app.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.repository.NewsBodyRepository;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;
import ru.plus.irbis.web.app.repository.NewsTopicRepository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NewsBodyRepositoryTest extends IntegrationTestBase {

    private static final Integer NEWS_ID = 7;
    private static final Integer TOPIC_ID = 2;
    private final NewsBodyRepository newsBodyRepository;
    private final NewsTopicRepository newsTopicRepository;
    private final NewsSourceRepository newsSourceRepository;

    @Test
    void findAll() {
        Page<NewsBody> news = newsBodyRepository.findAll(PageRequest.of(1, 3, Sort.by("id")));
        assertEquals(3, news.getSize());
        assertEquals(22, news.getTotalElements());
    }

    @Test
    void findBySource() {
        Page<NewsBody> news = newsBodyRepository.findBySource("irbis.plus", PageRequest.of(0, 3, Sort.by("id")));
        assertEquals(3, news.getSize());
        assertEquals(14, news.getTotalElements());
    }

    @Test
    void findByTopicId() {
        Page<NewsBody> news = newsBodyRepository.findByTopicId(TOPIC_ID, PageRequest.of(0, 3, Sort.by("id")));
        assertEquals(3, news.getSize());
        assertEquals(3, news.getTotalElements());
        assertEquals("База всех судов России с 2005 года с карточками дел.", news.getContent().get(1).getTitle());
    }

    @Test
//    @Transactional
    void save() {
        NewsBody newsBody = NewsBody.builder()
                .title("Test title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        Optional<NewsSource> sourceByName = newsSourceRepository.findByName("irbis.plus");
        assertTrue(sourceByName.isPresent());
        sourceByName.ifPresent(source -> {
            NewsTopic topic = newsTopicRepository.findByNameAndSourceId("О нас", source.getId());
            assertNotNull(topic);
            newsBody.setNewsTopic(topic);
            NewsBody actualNewsBody = newsBodyRepository.save(newsBody);
            assertEquals("Test title", actualNewsBody.getTitle());
        });
    }

    @Test
//    @Transactional
    void update() {
        NewsBody newsBody = NewsBody.builder()
                .title("Test title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        Optional<NewsBody> newsById = newsBodyRepository.findById(NEWS_ID);
        assertTrue(newsById.isPresent());

        Optional<NewsSource> sourceByName = newsSourceRepository.findByName("irbis.plus");
        assertTrue(sourceByName.isPresent());

        NewsTopic topic = newsTopicRepository.findByNameAndSourceId("О нас", sourceByName.get().getId());
        assertNotNull(topic);

        newsBody.setId(NEWS_ID);
        newsBody.setNewsTopic(topic);
        NewsBody actualNewsBody = newsBodyRepository.saveAndFlush(newsBody);

        Optional<NewsBody> updatedNewsById = newsBodyRepository.findById(NEWS_ID);
        assertTrue(updatedNewsById.isPresent());
        updatedNewsById.ifPresent(news -> {
            assertEquals(news.getId(), actualNewsBody.getId());
            assertEquals(news.getTitle(), actualNewsBody.getTitle());
            assertEquals(news.getUrlNews(), actualNewsBody.getUrlNews());
            assertEquals(news.getPublicDate(), actualNewsBody.getPublicDate());
            assertEquals(news.getNewsTopic().getId(), actualNewsBody.getNewsTopic().getId());
        });
    }

    @Test
    void delete() {
        Optional<NewsBody> news = newsBodyRepository.findById(NEWS_ID);
        assertTrue(news.isPresent());
        news.ifPresent(newsBodyRepository::delete);

        Optional<NewsBody> deletedNews = newsBodyRepository.findById(NEWS_ID);
        assertFalse(deletedNews.isPresent());
    }
}
package ru.plus.irbis.web.app.integration.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.service.NewsService;
import ru.plus.irbis.web.app.service.UserService;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIndexOutOfBoundsException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class NewsServiceTest extends IntegrationTestBase {

    private static final Integer SOURCE_ID = 1;
    private static final Integer TOPIC_ID = 2;
    private static final Integer NEWS_ID = 7;

    private final NewsService newsService;

    @Test
    void getAllSources() {
        List<NewsSource> sources = newsService.getAllSources();
        assertEquals(3, sources.size());
    }

    @Test
    void findSourceById() {
        Optional<NewsSource> sourceById = newsService.findSourceById(SOURCE_ID);
        assertTrue(sourceById.isPresent());
        sourceById.ifPresent(source -> assertEquals("irbis.plus", source.getName()));
    }

    @Test
    void createSource() {
        NewsSource newsSource = NewsSource.builder()
                .name("Test source")
                .url("url")
                .topics(new ArrayList<>(0))
                .build();
        NewsSource actualSource = newsService.createSource(newsSource);

        assertEquals(newsSource.getName(), actualSource.getName());
        assertEquals(newsSource.getUrl(), actualSource.getUrl());
    }

    @Test
    void updateSource() {
        NewsSource newsSource = NewsSource.builder()
                .id(SOURCE_ID)
                .name("Test source")
                .url("url")
                .topics(new ArrayList<>(0))
                .build();
        Optional<NewsSource> actualSource = newsService.updateSource(SOURCE_ID, newsSource);

        assertTrue(actualSource.isPresent());
        actualSource.ifPresent(source -> {
            assertEquals(newsSource.getId(), source.getId());
            assertEquals(newsSource.getName(), source.getName());
            assertEquals(newsSource.getUrl(), source.getUrl());
        });
    }

    @Test
    void deleteSource() {
        assertTrue(newsService.deleteSource(SOURCE_ID));
        assertFalse(newsService.deleteSource(-2));
    }

    @Test
    void getAllTopics() {
        List<NewsTopic> topics = newsService.getAllTopics();
        assertEquals(8, topics.size());
    }

    @Test
    void findTopicById() {
        Optional<NewsTopic> topicById = newsService.findTopicById(TOPIC_ID);
        assertTrue(topicById.isPresent());
        topicById.ifPresent(topic -> assertEquals("Помощь физ. лицам", topic.getName()));
    }

    @Test
    void createTopic() {
        NewsTopic newsTopic = NewsTopic.builder()
                .name("Test topic")
                .sourceId(SOURCE_ID)
                .bodies(new ArrayList<>(0))
                .build();
        NewsTopic actualTopic = newsService.createTopic(newsTopic);
        assertEquals(newsTopic.getName(), actualTopic.getName());
        assertEquals(newsTopic.getSourceId(), actualTopic.getNewsSource().getId());
    }

    @Test
    void updateTopic() {
        NewsTopic newsTopic = NewsTopic.builder()
                .id(TOPIC_ID)
                .name("Test topic")
                .sourceId(SOURCE_ID)
                .bodies(new ArrayList<>(0))
                .build();
        Optional<NewsTopic> actualTopic = newsService.updateTopic(TOPIC_ID, newsTopic);
        assertTrue(actualTopic.isPresent());
        actualTopic.ifPresent(topic -> {
            assertEquals(newsTopic.getId(), topic.getId());
            assertEquals(newsTopic.getName(), topic.getName());
            assertEquals(newsTopic.getSourceId(), topic.getNewsSource().getId());
        });
    }

    @Test
    void deleteTopic() {
        assertTrue(newsService.deleteTopic(TOPIC_ID));
        assertFalse(newsService.deleteTopic(-2));
    }

    @Test
    void getAllNews() {
        Page<NewsBody> news = newsService.getAllNews(PageRequest.of(0, 3, Sort.by("id")));
        assertEquals(3, news.getSize());
        assertEquals(22, news.getTotalElements());
    }

    @Test
    void getNewsBySource() {
        Page<NewsBody> news = newsService.getNewsBySource("vacancies.irbis.plus", PageRequest.of(0, 3, Sort.by("id")));
        assertEquals(3, news.getSize());
        assertEquals(3, news.getTotalElements());
    }

    @Test
    void getNewsByTopic() {
        Page<NewsBody> news = newsService.getNewsByTopic(TOPIC_ID, PageRequest.of(0, 2, Sort.by("id")));
        assertEquals("База всех судов России с 2005 года с карточками дел.", news.getContent().get(1).getTitle());
        assertEquals(2, news.getSize());
        assertEquals(3, news.getTotalElements());
    }

    @Test
    void createNews() {
        NewsBody newsBody = NewsBody.builder()
                .topicId(TOPIC_ID)
                .title("Test title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();
        Optional<NewsBody> actualNews = newsService.createNews(newsBody, "Помощь физ. лицам", "irbis.plus");
        actualNews.ifPresent(news -> {
            assertEquals(newsBody.getTitle(), news.getTitle());
            assertEquals(newsBody.getUrlNews(), news.getUrlNews());
            assertEquals(newsBody.getPublicDate(), news.getPublicDate());
        });

    }

    @Test
    void updateNewsBody() {
        NewsBody newsBody = NewsBody.builder()
                .id(NEWS_ID)
                .topicId(TOPIC_ID)
                .title("Test title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();
        Optional<NewsBody> actualNews = newsService.updateNewsBody(NEWS_ID, newsBody, "Помощь физ. лицам", "irbis.plus");

        actualNews.ifPresent(news -> {
            assertEquals(newsBody.getId(), news.getId());
            assertEquals(newsBody.getTitle(), news.getTitle());
            assertEquals(newsBody.getUrlNews(), news.getUrlNews());
            assertEquals(newsBody.getPublicDate(), news.getPublicDate());
            assertEquals(NEWS_ID, news.getId());
        });
    }

    @Test
    void deleteNewsBody() {
        assertTrue(newsService.deleteNewsBody(NEWS_ID));
        assertFalse(newsService.deleteNewsBody(-2));
    }
}
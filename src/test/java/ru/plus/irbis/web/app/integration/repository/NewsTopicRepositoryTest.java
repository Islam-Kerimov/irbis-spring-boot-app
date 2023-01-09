package ru.plus.irbis.web.app.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;
import ru.plus.irbis.web.app.repository.NewsTopicRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NewsTopicRepositoryTest extends IntegrationTestBase {

    private final NewsTopicRepository newsTopicRepository;
    private final NewsSourceRepository newsSourceRepository;

    @Test
    void findByNameAndSourceId() {
        NewsTopic topicByNameAndSourceId = newsTopicRepository.findByNameAndSourceId("Помощь юр. лицам", 1);
        assertEquals(1, topicByNameAndSourceId.getId());
    }

    @Test
    void findAll() {
        List<NewsTopic> topics = newsTopicRepository.findAll(Sort.by("id"));
        assertThat(topics).hasSize(8);
    }

    @Test
    void findById() {
        Optional<NewsTopic> topic = newsTopicRepository.findById(4);
        assertTrue(topic.isPresent());
        topic.ifPresent(t -> assertEquals("О нас", t.getName()));
    }

    @Test
//    @Transactional
    void save() {
        Optional<NewsSource> source = newsSourceRepository.findById(1);
        assertTrue(source.isPresent());
        NewsTopic newTopic = newsTopicRepository.save(NewsTopic.builder()
                .newsSource(source.get())
                .name("New topic")
                .build());
        assertEquals("New topic", newTopic.getName());

        List<NewsTopic> topics = newsTopicRepository.findAll();
        assertThat(topics).hasSize(9);
    }

    @Test
//    @Transactional
    void update() {
        Optional<NewsSource> source = newsSourceRepository.findById(1);
        assertTrue(source.isPresent());

        NewsTopic newTopic = NewsTopic.builder()
                .id(2)
                .newsSource(source.get())
                .name("О нас")
                .build();
        newsTopicRepository.saveAndFlush(newTopic);

        Optional<NewsTopic> updatedTopic = newsTopicRepository.findById(2);
        updatedTopic.ifPresent(topic -> assertEquals("О нас", topic.getName()));
    }

    @Test
    void delete() {
        Optional<NewsTopic> topic = newsTopicRepository.findById(1);
        assertTrue(topic.isPresent());
        topic.ifPresent(newsTopicRepository::delete);

        Optional<NewsTopic> deletedNewsTopic = newsTopicRepository.findById(1);
        assertFalse(deletedNewsTopic.isPresent());
    }
}
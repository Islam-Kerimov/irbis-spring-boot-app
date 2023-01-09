package ru.plus.irbis.web.app.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class NewsSourceRepositoryTest extends IntegrationTestBase {

    private final NewsSourceRepository newsSourceRepository;

    @Test
    void findByName() {
        Optional<NewsSource> sourceByName = newsSourceRepository.findByName("praktika.irbis.plus");
        assertTrue(sourceByName.isPresent());
    }

    @Test
    void findAll() {
        List<NewsSource> sources = newsSourceRepository.findAll(Sort.by("id"));
        assertThat(sources).hasSize(3);
    }

    @Test
    void findById() {
        Optional<NewsSource> source = newsSourceRepository.findById(1);
        assertTrue(source.isPresent());
        source.ifPresent(s -> assertEquals("https://irbis.plus.ru", s.getUrl()));
    }

    @Test
    void save() {
        NewsSource newsSource = newsSourceRepository.save(NewsSource.builder()
                .name("New source")
                .url("url")
                .build());
        assertEquals("url", newsSource.getUrl());
        List<NewsSource> sources = newsSourceRepository.findAll();
        assertThat(sources).hasSize(4);
    }

    @Test
    void update() {
        Optional<NewsSource> newsSource = newsSourceRepository.findById(1);
        assertTrue(newsSource.isPresent());
        newsSource.get().setName("New name");
        newsSource.ifPresent(newsSourceRepository::saveAndFlush);

        Optional<NewsSource> updatedNewsSource = newsSourceRepository.findById(1);
        updatedNewsSource.ifPresent(source -> assertEquals("New name", source.getName()));
    }

    @Test
    void delete() {
        Optional<NewsSource> newsSource = newsSourceRepository.findById(1);
        assertTrue(newsSource.isPresent());
        newsSource.ifPresent(newsSourceRepository::delete);

        Optional<NewsSource> deletedNewsSource = newsSourceRepository.findById(1);
        assertFalse(deletedNewsSource.isPresent());
    }
}
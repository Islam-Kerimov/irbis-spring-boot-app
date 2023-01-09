package ru.plus.irbis.web.app.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.plus.irbis.web.app.integration.IntegrationTestBase;
import ru.plus.irbis.web.app.model.dto.NewsBodyDto;
import ru.plus.irbis.web.app.model.dto.NewsSourceDto;
import ru.plus.irbis.web.app.model.dto.NewsTopicDto;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.model.mapper.NewsMapper;
import ru.plus.irbis.web.app.repository.NewsBodyRepository;
import ru.plus.irbis.web.app.repository.NewsSourceRepository;
import ru.plus.irbis.web.app.repository.NewsTopicRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "admin", password = "1234", authorities = {"ADMIN"})
public class NewsControllerTest extends IntegrationTestBase {

    private static final Integer SOURCE_ID = 1;
    private static final Integer TOPIC_ID = 2;
    private static final Integer NEWS_ID = 7;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final NewsSourceRepository newsSourceRepository;
    private final NewsTopicRepository newsTopicRepository;
    private final NewsBodyRepository newsBodyRepository;

    @Test
    void getAllSources() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/sources"));

        response.andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(3)));
    }

    @Test
    void getSourceById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/sources/{id}", SOURCE_ID));

        response.andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.name", is("irbis.plus")))
                .andExpect(jsonPath("$.url", is("https://irbis.plus.ru")));
    }

    @Test
    void invalidGetSourceById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/sources/{id}", -SOURCE_ID));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void createSource() throws Exception {
        NewsSourceDto newSourceDto = NewsSourceDto.builder().name("test").url("test").build();

        ResultActions response = mockMvc.perform(post("/api/v1/sources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newSourceDto)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.url", is("test")));

        List<NewsSource> all = newsSourceRepository.findAll();
        assertEquals(4, all.size());
    }

    @Test
    void updateSource() throws Exception {
        NewsSourceDto newsSourceDto = NewsSourceDto.builder().id(SOURCE_ID).name("test").url("test").build();

        ResultActions response = mockMvc.perform(put("/api/v1/sources/{id}", SOURCE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsSourceDto)));

        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name", is(newsSourceDto.getName())))
                .andExpect(jsonPath("$.url", is(newsSourceDto.getUrl())));

        Optional<NewsSource> sourceById = newsSourceRepository.findById(SOURCE_ID);
        assertTrue(sourceById.isPresent());
        sourceById.ifPresent(source -> {
            assertEquals(source.getUrl(), newsSourceDto.getUrl());
            assertEquals(source.getName(), newsSourceDto.getName());
        });
    }

    @WithMockUser(username = "user", password = "1234", authorities = {"USER"})
    @Test
    void invalidUpdateSourceWithUser() throws Exception {
        NewsSourceDto newsSourceDto = NewsSourceDto.builder().id(SOURCE_ID).name("test").url("test").build();

        ResultActions response = mockMvc.perform(put("/api/v1/sources/{id}", SOURCE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsSourceDto)));

        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void invalidUpdateSource() throws Exception {
        NewsSourceDto newsSourceDto = NewsSourceDto.builder().id(SOURCE_ID).name("test").url("test").build();

        ResultActions response = mockMvc.perform(put("/api/v1/sources/{id}", -SOURCE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsSourceDto)));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deleteSource() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/v1/sources/{id}", SOURCE_ID));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllTopics() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/topics"));

        response.andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(8)));
    }

    @Test
    void getTopicById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/topics/{id}", TOPIC_ID));

        response.andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andExpect(jsonPath("$.source_id", is(SOURCE_ID)))
                .andExpect(jsonPath("$.name", is("Помощь физ. лицам")));
    }

    @Test
    void invalidGetTopicById() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/topics/{id}", -TOPIC_ID));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void createTopic() throws Exception {
        NewsTopicDto newsTopicDto = NewsTopicDto.builder().sourceId(SOURCE_ID).name("test topic").build();

        ResultActions response = mockMvc.perform(post("/api/v1/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsTopicDto)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.source_id", is(SOURCE_ID)))
                .andExpect(jsonPath("$.name", is("test topic")));

        List<NewsTopic> all = newsTopicRepository.findAll();
        assertEquals(9, all.size());
    }

    @Test
    void updateTopic() throws Exception {
        NewsTopicDto newsTopicDto = NewsTopicDto.builder().sourceId(SOURCE_ID).name("test topic").build();

        ResultActions response = mockMvc.perform(put("/api/v1/topics/{id}", TOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsTopicDto)));

        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.source_id", is(newsTopicDto.getSourceId())))
                .andExpect(jsonPath("$.name", is(newsTopicDto.getName())));

        Optional<NewsTopic> topicById = newsTopicRepository.findById(TOPIC_ID);
        assertTrue(topicById.isPresent());
        topicById.ifPresent(topic -> {
            assertEquals(topic.getSourceId(), newsTopicDto.getSourceId());
            assertEquals(topic.getName(), newsTopicDto.getName());
        });
    }

    @Test
    void invalidUpdateTopic() throws Exception {
        NewsTopicDto newsTopicDto = NewsTopicDto.builder().id(TOPIC_ID).sourceId(SOURCE_ID).name("test topic").build();

        ResultActions response = mockMvc.perform(put("/api/v1/topics/{id}", -TOPIC_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsTopicDto)));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @WithMockUser(username = "user", password = "1234", authorities = {"USER"})
    @Test
    void deleteTopic() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/v1/topics/{id}", TOPIC_ID));

        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void invalidDeleteTopicWithUser() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/v1/topics/{id}", TOPIC_ID));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllNews() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/news")
                .param("s", "irbis.plus")
                .param("page", "0")
                .param("size", "1"));

        response.andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    void getNewsBySource() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/news/source")
                .param("s", "irbis.plus")
                .param("page", "0")
                .param("size", "5"));

        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getNewsByTopic() throws Exception {
        ResultActions response = mockMvc.perform(get("/api/v1/news/topic")
                .param("id", "2")
                .param("page", "0")
                .param("size", "5"));

        response.andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void createNews() throws Exception {
        NewsBodyDto newsBodyDto = NewsBodyDto.builder()
                .source("irbis.plus")
                .topic("Помощь физ. лицам")
                .title("title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(post("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsBodyDto)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(newsBodyDto.getTitle())));

        List<NewsBody> all = newsBodyRepository.findAll();
        assertEquals(23, all.size());
    }

    @WithMockUser(username = "user", password = "1234", authorities = {"USER"})
    @Test
    void unvalidCreateNewsWithUser() throws Exception {
        NewsBodyDto newsBodyDto = NewsBodyDto.builder()
                .source("irbis.plus")
                .topic("Помощь физ. лицам")
                .title("title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(post("/api/v1/news")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsBodyDto)));

        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void updateNews() throws Exception {
        NewsBodyDto newsBodyDto = NewsBodyDto.builder()
                .source("irbis.plus")
                .topic("Помощь физ. лицам")
                .title("title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(put("/api/v1/news/{id}", NEWS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsBodyDto)));

        response.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.title", is(newsBodyDto.getTitle())))
                .andExpect(jsonPath("$.url", is(newsBodyDto.getUrlNews())));

        Optional<NewsBody> newsById = newsBodyRepository.findById(NEWS_ID);
        assertTrue(newsById.isPresent());
        newsById.ifPresent(topic -> {
            assertEquals(topic.getUrlNews(), newsBodyDto.getUrlNews());
            assertEquals(topic.getTitle(), newsBodyDto.getTitle());
        });
    }

    @Test
    void invalidUpdateNews() throws Exception {
        NewsBodyDto newsBodyDto = NewsBodyDto.builder()
                .source("irbis.plus")
                .topic("Помощь физ. лицам")
                .title("title")
                .urlNews("url")
                .publicDate(OffsetDateTime.now())
                .build();

        ResultActions response = mockMvc.perform(put("/api/v1/news/{id}", -NEWS_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newsBodyDto)));

        response.andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    void deleteNews() throws Exception {
        ResultActions response = mockMvc.perform(delete("/api/v1/news/{id}", NEWS_ID));

        response.andDo(print())
                .andExpect(status().isNoContent());
    }
}

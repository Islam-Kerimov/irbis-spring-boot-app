package ru.plus.irbis.web.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.plus.irbis.web.app.model.dto.NewsBodyDto;
import ru.plus.irbis.web.app.model.dto.NewsSourceDto;
import ru.plus.irbis.web.app.model.dto.NewsTopicDto;
import ru.plus.irbis.web.app.model.dto.PageResponse;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;
import ru.plus.irbis.web.app.model.mapper.NewsMapper;
import ru.plus.irbis.web.app.service.NewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@RestController
@RequestMapping(value = "/api/v1")
@Validated
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "News", description = "The News API. Contains all the operations to display news data.")
public class NewsController {

    private static final String EXCEPTION_FORMAT = "There is no %s with ID = %d in Database";

    private final NewsService newsService;

    private final NewsMapper newsMapper;

    public NewsController(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    @GetMapping("/sources")
    @Operation(summary = "Get all sources")
    public List<NewsSourceDto> getAllSources() {
        List<NewsSource> allSources = newsService.getAllSources();
        return newsMapper.entityNewsSourceListToDtoList(allSources);
    }

    @GetMapping("/sources/{id}")
    @Operation(summary = "Get sources by id")
    public NewsSourceDto getSourceById(@PathVariable @Min(1) Integer id) {
        Optional<NewsSource> sourceById = newsService.findSourceById(id);
        return newsMapper.entityNewsSourceToDto(sourceById
                .orElseThrow(() -> new NoSuchElementException(format(EXCEPTION_FORMAT, "source", id))));
    }

    @PostMapping("/sources")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create source")
    public NewsSourceDto createSource(@RequestBody @Validated NewsSourceDto sourceDto) {
        NewsSource newsSource = newsService.createSource(newsMapper.dtoNewsSourceToEntity(sourceDto));
        return newsMapper.entityNewsSourceToDto(newsSource);
    }

    @PutMapping("/sources/{id}")
    @Operation(summary = "Update source by id")
    public NewsSourceDto updateSource(@PathVariable @Min(1) Integer id,
                                      @RequestBody @Validated NewsSourceDto sourceDto) {
        Optional<NewsSource> newsSource =
                newsService.updateSource(id, newsMapper.dtoNewsSourceToEntity(sourceDto));
        return newsMapper.entityNewsSourceToDto(newsSource
                .orElseThrow(() -> new NoSuchElementException(format(EXCEPTION_FORMAT, "source", id))));
    }

    @DeleteMapping("/sources/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete sources by id")
    public void deleteSource(@PathVariable @Min(1) Integer id) {
        if (!newsService.deleteSource(id)) {
            throw new NoSuchElementException(format(EXCEPTION_FORMAT, "source", id));
        }
    }

    @GetMapping("/topics")
    @Operation(summary = "Get all topics")
    public List<NewsTopicDto> getAllTopics() {
        List<NewsTopic> allTopics = newsService.getAllTopics();
        return newsMapper.entityNewsTopicListToDtoList(allTopics);
    }

    @GetMapping("/topics/{id}")
    @Operation(summary = "Get topic by id")
    public NewsTopicDto getTopicById(@PathVariable @Min(1) Integer id) {
        Optional<NewsTopic> topicById = newsService.findTopicById(id);
        return newsMapper.entityNewsTopicToDto(topicById
                .orElseThrow(() -> new NoSuchElementException(format(EXCEPTION_FORMAT, "topic", id))));
    }

    @PostMapping("/topics")
    @Operation(summary = "Create topic")
    @ResponseStatus(HttpStatus.CREATED)
    public NewsTopicDto createTopic(@RequestBody @Validated NewsTopicDto topicDto) {
        NewsTopic newsTopic =
                newsService.createTopic(newsMapper.dtoNewsTopicToEntity(topicDto));
        return newsMapper.entityNewsTopicToDto(newsTopic);
    }

    @PutMapping("/topics/{id}")
    @Operation(summary = "Update topic by id")
    public NewsTopicDto updateTopic(@PathVariable @Min(1) Integer id,
                                    @RequestBody @Validated NewsTopicDto topicDto) {
        Optional<NewsTopic> newsTopic =
                newsService.updateTopic(id, newsMapper.dtoNewsTopicToEntity(topicDto));
        return newsMapper.entityNewsTopicToDto(newsTopic
                .orElseThrow(() -> new NoSuchElementException(format(EXCEPTION_FORMAT, "topic", id))));
    }

    @DeleteMapping("/topics/{id}")
    @Operation(summary = "Delete topic by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable @Min(1) Integer id) {
        if (!newsService.deleteTopic(id)) {
            throw new NoSuchElementException(format(EXCEPTION_FORMAT, "topic", id));
        }
    }

    @GetMapping("/news")
    @Operation(summary = "Get all news")
    public PageResponse<NewsBodyDto> getAllNews(
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "3") @Min(1) Integer size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        List<Order> orders = getOrders(sort);
        Page<NewsBody> newsBodies = newsService.getAllNews(PageRequest.of(page, size, Sort.by(orders)));
        List<NewsBodyDto> news = newsMapper.entityNewsBodyListToDtoList(newsBodies.getContent());
        return PageResponse.of(news, newsBodies);
    }

    @GetMapping("/news/source")
    @Operation(summary = "Get all news by source name")
    public PageResponse<NewsBodyDto> getNewsBySource(
            @RequestParam("s") String source,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "3") @Min(1) Integer size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        List<Order> orders = getOrders(sort);
        Page<NewsBody> newsBySource =
                newsService.getNewsBySource(source, PageRequest.of(page, size, Sort.by(orders)));
        List<NewsBodyDto> news =
                newsMapper.entityNewsBodyListToDtoList(newsBySource.getContent());
        return PageResponse.of(news, newsBySource);
    }

    @GetMapping("/news/topic")
    @Operation(summary = "Get all news by topic id")
    public PageResponse<NewsBodyDto> getNewsByTopic(
            @RequestParam("id") @Min(1) Integer topicId,
            @RequestParam(defaultValue = "0") @Min(0) Integer page,
            @RequestParam(defaultValue = "3") @Min(1) Integer size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {

        List<Order> orders = getOrders(sort);
        Page<NewsBody> newsByTopicId =
                newsService.getNewsByTopic(topicId, PageRequest.of(page, size, Sort.by(orders)));
        List<NewsBodyDto> news =
                newsMapper.entityNewsBodyListToDtoList(newsByTopicId.getContent());
        return PageResponse.of(news, newsByTopicId);
    }

    private List<Order> getOrders(String[] sort) {
        List<Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            for (String sortOrder : sort) {
                String[] sortValue = sortOrder.split(",");
                orders.add(new Order(getSortDirection(sortValue[1]), sortValue[0]));
            }
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }

        return orders;
    }

    @PostMapping("/news")
    @Operation(summary = "Create news")
    @ResponseStatus(HttpStatus.CREATED)
    public NewsBodyDto createNews(@RequestBody @Validated NewsBodyDto newsBodyDto) {
        Optional<NewsBody> newsBody = newsService.createNews(
                newsMapper.dtoNewsBodyToEntity(newsBodyDto),
                newsBodyDto.getTopic(),
                newsBodyDto.getSource()
        );
        return newsMapper.entityNewsBodyToDto(newsBody
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PutMapping("/news/{id}")
    @Operation(summary = "Update news by id")
    public NewsBodyDto updateNews(@PathVariable @Min(1) Integer id,
                                  @RequestBody @Validated NewsBodyDto newsBodyDto) {
        Optional<NewsBody> newsBody = newsService.updateNewsBody(
                id,
                newsMapper.dtoNewsBodyToEntity(newsBodyDto),
                newsBodyDto.getTopic(),
                newsBodyDto.getSource()
        );
        return newsMapper.entityNewsBodyToDto(newsBody
                .orElseThrow(() -> new NoSuchElementException(format(EXCEPTION_FORMAT, "news", id))));
    }

    @DeleteMapping("/news/{id}")
    @Operation(summary = "Delete news by id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable @Min(1) Integer id) {
        if (!newsService.deleteNewsBody(id)) {
            throw new NoSuchElementException(format(EXCEPTION_FORMAT, "news", id));
        }
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equalsIgnoreCase("asc")) {
            return ASC;
        } else if (direction.equalsIgnoreCase("desc")) {
            return DESC;
        }

        return ASC;
    }
}

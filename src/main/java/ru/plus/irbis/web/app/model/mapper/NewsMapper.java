package ru.plus.irbis.web.app.model.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.plus.irbis.web.app.model.dto.NewsBodyDto;
import ru.plus.irbis.web.app.model.dto.NewsSourceDto;
import ru.plus.irbis.web.app.model.dto.NewsTopicDto;
import ru.plus.irbis.web.app.model.entity.NewsBody;
import ru.plus.irbis.web.app.model.entity.NewsSource;
import ru.plus.irbis.web.app.model.entity.NewsTopic;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    @Named("Source")
    NewsSourceDto entityNewsSourceToDto(NewsSource newsSource);

    NewsSource dtoNewsSourceToEntity(NewsSourceDto newsSourceDto);

    @IterableMapping(qualifiedByName = "Source")
    List<NewsSourceDto> entityNewsSourceListToDtoList(List<NewsSource> sources);

    @Named("Topic")
    NewsTopicDto entityNewsTopicToDto(NewsTopic newsTopic);

    NewsTopic dtoNewsTopicToEntity(NewsTopicDto topicDto);

    @IterableMapping(qualifiedByName = "Topic")
    List<NewsTopicDto> entityNewsTopicListToDtoList(List<NewsTopic> topics);

    @Named("News")
    @Mapping(target = "source", source = "newsTopic.newsSource.name")
    @Mapping(target = "topic", source = "newsTopic.name")
    NewsBodyDto entityNewsBodyToDto(NewsBody newsBody);

    @IterableMapping(qualifiedByName = "News")
    List<NewsBodyDto> entityNewsBodyListToDtoList(List<NewsBody> news);

    NewsBody dtoNewsBodyToEntity(NewsBodyDto newsBodyDto);
}

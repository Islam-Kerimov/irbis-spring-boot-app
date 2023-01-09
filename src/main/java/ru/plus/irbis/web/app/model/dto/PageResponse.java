package ru.plus.irbis.web.app.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
public class PageResponse<T> {
    List<T> content;
    Metadata metadata;

    public static <T> PageResponse<T> of(List<T> content, Page<?> page) {
        Metadata metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements());
        return new PageResponse<>(content, metadata);
    }

    @Data
    @AllArgsConstructor
    public static class Metadata {
        int page;
        int size;
        long totalElements;
    }
}

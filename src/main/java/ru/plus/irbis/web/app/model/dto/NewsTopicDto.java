package ru.plus.irbis.web.app.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Builder
public class NewsTopicDto {

    @JsonProperty(access = READ_ONLY)
    private Integer id;

    @NotNull
    @Min(1)
    @JsonProperty("source_id")
    private Integer sourceId;

    @NotNull
    private String name;
}

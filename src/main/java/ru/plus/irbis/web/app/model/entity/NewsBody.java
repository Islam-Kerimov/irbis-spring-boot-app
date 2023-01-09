package ru.plus.irbis.web.app.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "news_body")
public class NewsBody implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "topic_id", insertable = false, updatable = false)
    private Integer topicId;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private NewsTopic newsTopic;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String urlNews;

    @Column(nullable = false)
    private OffsetDateTime publicDate;
}

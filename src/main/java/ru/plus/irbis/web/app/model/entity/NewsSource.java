package ru.plus.irbis.web.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "topics")
@Entity
@Table(name = "news_source")
public class NewsSource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "source_id")
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String url;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "newsSource", orphanRemoval = true)
    private List<NewsTopic> topics = new ArrayList<>(0);
}

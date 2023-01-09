package ru.plus.irbis.web.app.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "bodies")
@Builder
@Entity
@Table(name = "news_topic")
public class NewsTopic implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Integer id;

    @Column(name = "source_id", insertable = false, updatable = false)
    private Integer sourceId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private NewsSource newsSource;

    @Column(unique = true, nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "newsTopic", orphanRemoval = true)
    private List<NewsBody> bodies = new ArrayList<>(0);
}

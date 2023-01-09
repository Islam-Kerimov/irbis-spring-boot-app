package ru.plus.irbis.web.app.model.entity;

import lombok.*;

import jakarta.persistence.*;

/**
 * Параметры планирования задач.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_properties")
public class TaskProperties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String cronExpression;

    @Column
    private String options;
}

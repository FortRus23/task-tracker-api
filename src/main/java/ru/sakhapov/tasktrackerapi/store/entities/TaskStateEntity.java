package ru.sakhapov.tasktrackerapi.store.entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE) // experimental
@Entity
@Builder
@Table(name = "task_state")
public class TaskStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String name;

    @Builder.Default
    Instant createdAt = Instant.now();

    Long ordinal;

    @OneToMany
    @JoinColumn(name = "task_state_id", referencedColumnName = "id")
    @Builder.Default
    List<TaskEntity> tasks = new ArrayList<>();

    @ManyToOne
    ProjectEntity project;


}

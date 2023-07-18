package ru.sakhapov.tasktrackerapi.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sakhapov.tasktrackerapi.store.entities.TaskStateEntity;

public interface TaskRepository extends JpaRepository<TaskStateEntity, Long> {
}

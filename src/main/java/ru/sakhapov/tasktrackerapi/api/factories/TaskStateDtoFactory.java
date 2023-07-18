package ru.sakhapov.tasktrackerapi.api.factories;

import org.springframework.stereotype.Component;
import ru.sakhapov.tasktrackerapi.api.dto.TaskStateDto;
import ru.sakhapov.tasktrackerapi.store.entities.TaskStateEntity;

@Component
public class TaskStateDtoFactory {

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity){

        return TaskStateDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .createdAt(entity.getCreatedAt())
                .ordinal(entity.getOrdinal())
                .build();
    }
}

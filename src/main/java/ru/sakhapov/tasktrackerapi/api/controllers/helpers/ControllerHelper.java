package ru.sakhapov.tasktrackerapi.api.controllers.helpers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sakhapov.tasktrackerapi.api.exceptions.NotFoundException;
import ru.sakhapov.tasktrackerapi.store.entities.ProjectEntity;
import ru.sakhapov.tasktrackerapi.store.repositories.ProjectRepository;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository.
                findById(projectId)
                .orElseThrow(() -> new NotFoundException(
                                String.format(
                                        "Project with '%s' this id not found.",
                                        projectId
                                )
                        )
                );
    }

}

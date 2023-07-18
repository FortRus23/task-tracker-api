package ru.sakhapov.tasktrackerapi.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.sakhapov.tasktrackerapi.api.dto.AckDto;
import ru.sakhapov.tasktrackerapi.api.exceptions.NotFoundException;
import ru.sakhapov.tasktrackerapi.api.dto.ProjectDto;
import ru.sakhapov.tasktrackerapi.api.exceptions.BadRequestException;
import ru.sakhapov.tasktrackerapi.api.factories.ProjectDtoFactory;
import ru.sakhapov.tasktrackerapi.store.entities.ProjectEntity;
import ru.sakhapov.tasktrackerapi.store.repositories.ProjectRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProjectController {

    ProjectRepository projectRepository;

    ProjectDtoFactory projectDtoFactory;

    public static final String CREATE_PROJECT = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";
    public static final String FETCH_PROJECTS = "/api/projects";
    public static final String EDIT_PROJECT = "/api/projects/{project_id}";


    @PostMapping(CREATE_PROJECT)
    public ProjectDto createProject(@RequestParam String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        projectRepository
                .findByName(name)
                .ifPresent(project -> {
                    throw new BadRequestException(
                            String.format("Project with name '%s' already exists.", name)
                    );
                });

        ProjectEntity project = projectRepository.saveAndFlush(
                ProjectEntity.builder()
                        .name(name)
                        .build()
        );

        return projectDtoFactory.makeProjectDto(project);
    }


    @PatchMapping(EDIT_PROJECT)
    public ProjectDto editProject(@PathVariable("project_id") Long projectId,
                                  @RequestParam String name) {

        if (name.trim().isEmpty()) {
            throw new BadRequestException("Project name can't be empty.");
        }

        ProjectEntity project = getProjectOrThrowException(projectId);

        projectRepository
                .findByName(name)
                .filter(elseProject -> !Objects.equals(elseProject.getId(), projectId))
                .ifPresent(elseProject -> {
                    throw new BadRequestException(
                            String.format("Project with name '%s' already exists.", name)
                    );
                });

        project.setName(name);

        project = projectRepository.saveAndFlush(project);

        return projectDtoFactory.makeProjectDto(project);
    }

    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName.filter(prefixName -> !prefixName.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        return projectStream.map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId) {

        getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }



    private ProjectEntity getProjectOrThrowException(Long projectId) {
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
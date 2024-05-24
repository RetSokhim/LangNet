package org.example.langnet.service;

import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.entity.Project;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectResponse createNewProject(ProjectCreationRequest projectCreationRequest);
    ProjectResponse getProjectById(UUID projectId);
    void deleteProjectIdByUpdateActive(UUID projectId);
    List<ProjectResponse> searchProjectByName(String projectName,UUID userId);
    List<ProjectResponse> getAllProjectByCurrentUser(UUID userId);
    void updateProjectStatus(UUID projectId);
}

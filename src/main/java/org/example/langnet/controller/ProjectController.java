package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.entity.Project;
import org.example.langnet.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/project")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createNewProject(@RequestBody ProjectCreationRequest projectCreationRequest){
        ProjectResponse project = projectService.createNewProject(projectCreationRequest);
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Created project successfully",
                HttpStatus.CREATED,project,201, LocalDateTime.now());
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable UUID projectId){
        ProjectResponse project = projectService.getProjectById(projectId);
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Created project successfully",
                HttpStatus.OK,
                project,201,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
}

package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.langnet.model.dto.request.AddMemberIntoProjectRequest;
import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.service.AppUserService;
import org.example.langnet.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/project")
@SecurityRequirement(name = "bearerAuth")
public class ProjectController {
    private final ProjectService projectService;
    private final AppUserService appUserService;

    public ProjectController(ProjectService projectService, AppUserService appUserService) {
        this.projectService = projectService;
        this.appUserService = appUserService;
    }

    @PostMapping
    @Operation(summary = "Create new Project")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody ProjectCreationRequest projectCreationRequest){
        ProjectResponse project = projectService.createNewProject(projectCreationRequest);
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Created project successfully",
                HttpStatus.CREATED,project,201, LocalDateTime.now());
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }

    @GetMapping("/GetProjectByID/{projectId}")
    @Operation(summary = "Search project by ID")
    public ResponseEntity<?> getProjectById(@PathVariable UUID projectId){
        ProjectResponse project = projectService.getProjectById(projectId);
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Project found successfully",
                HttpStatus.OK,
                project,201,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
    }
    @PutMapping("/DeleteProjectByID/{projectId}")
    @Operation(summary = "Delete project by ID by update active status")
    public ResponseEntity<?> deleteProjectIdByUpdateActive(@PathVariable UUID projectId) {
        projectService.deleteProjectIdByUpdateActive(projectId);
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Project deleted successfully",
                HttpStatus.OK,
                null, 201,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/SearchProjectByName")
    @Operation(summary = "Search project by project name but only member")
    public ResponseEntity<?> searchProjectByName(@RequestParam String projectName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.findUserByEmail(email);
        List<ProjectResponse> project = projectService.searchProjectByName(projectName,user.getUserId());
        ApiResponse<List<ProjectResponse>> apiResponse = new ApiResponse<>("Project deleted successfully",
                HttpStatus.OK,
                project, 200,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/GetAllProject")
    @Operation(summary = "Get current user all projects")
    public ResponseEntity<?> getAllProjectByCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.findUserByEmail(email);
        List<ProjectResponse> project = projectService.getAllProjectByCurrentUser(user.getUserId());
        ApiResponse<List<ProjectResponse>> apiResponse = new ApiResponse<>("Project deleted successfully",
                HttpStatus.OK,
                project, 200,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/UpdateStatus/{projectId}")
    @Operation(summary = "Update project status (Not done yet)")
    public ResponseEntity<?> updateProjectStatus(@PathVariable UUID projectId) {
        projectService.updateProjectStatus(projectId);
        ApiResponse<List<ProjectResponse>> apiResponse = new ApiResponse<>("Project deleted successfully",
                HttpStatus.OK,
                null, 200,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/AddMember")
    @Operation(summary = "Add project member")
    ResponseEntity<?> addNewMemberToProject(@Valid @RequestBody AddMemberIntoProjectRequest addMemberIntoProjectRequest){
        appUserService.addNewMemberToProject(addMemberIntoProjectRequest);
        return new ResponseEntity<>(new ApiResponse<>("Member Add Successfully", HttpStatus.OK,null,200, LocalDateTime.now()),HttpStatus.OK);
    }

}

package org.example.langnet.service.serviceimpl;

import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.dto.respond.UserInProjectResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.model.entity.Project;
import org.example.langnet.repository.AppUserRepository;
import org.example.langnet.repository.ProjectRepository;
import org.example.langnet.repository.RoleRepository;
import org.example.langnet.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    public ProjectServiceImpl(ProjectRepository projectRepository, AppUserRepository appUserRepository, RoleRepository roleRepository, ModelMapper mapper) {
        this.projectRepository = projectRepository;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.mapper = mapper;
    }

    @Override
    public ProjectResponse createNewProject(ProjectCreationRequest projectCreationRequest) {
        // Get the authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        // Find the user by email
        AppUser user = appUserRepository.findUserByEmail(email);
        // Get the role ID for "ProjectLeader"
        UUID roleId = roleRepository.getRoleByName("ProjectLeader");
        // Create a new project and set its name and users
        Project projectAfterInsert = projectRepository.createNewProject(projectCreationRequest);
        // Insert the user into the project member table
        projectRepository.insertMemberIntoProjectMember(projectAfterInsert.getProjectId(), user.getUserId(), roleId);
        projectAfterInsert.setProjectName(projectCreationRequest.getProjectName());
        List<AppUser> users = new ArrayList<>();
        users.add(user);
        projectAfterInsert.setUsers(users);
        // Map the project to ProjectResponse
        ProjectResponse projectResponse = mapper.map(projectAfterInsert, ProjectResponse.class);
        // Map each AppUser to UserInProjectResponse and set the role name
        List<UserInProjectResponse> userInProjectResponses = new ArrayList<>();
        for (AppUser appUser : projectAfterInsert.getUsers()) {
            UserInProjectResponse userInProjectResponse = mapper.map(appUser, UserInProjectResponse.class);
            String roleName = roleRepository.getRoleNameByUserIdAndProjectId(appUser.getUserId(),projectAfterInsert.getProjectId());
            userInProjectResponse.setRole(roleName);
            userInProjectResponses.add(userInProjectResponse);
        }
        // Set the users in the project response
        projectResponse.setUsers(userInProjectResponses);
        return projectResponse;
    }

    @Override
    public ProjectResponse getProjectById(UUID projectId) {
        Project project = projectRepository.getProjectById(projectId);
        return mapper.map(project,ProjectResponse.class);
    }

    @Override
    public void deleteProjectIdByUpdateActive(UUID projectId) {
        projectRepository.deleteProjectIdByUpdateActive(projectId);
    }

    @Override
    public List<ProjectResponse> searchProjectByName(String projectName,UUID userId) {
        List<Project> projects = projectRepository.searchProjectByName(projectName,userId);
        List<ProjectResponse> projectResponses = new ArrayList<>();
        for(Project project : projects){
            ProjectResponse projectResponse = mapper.map(project,ProjectResponse.class);
            projectResponses.add(projectResponse);
        }
        return projectResponses;
    }

    @Override
    public List<ProjectResponse> getAllProjectByCurrentUser(UUID userId) {
        List<Project> projects = projectRepository.getAllProjectByCurrentUser(userId);
        List<ProjectResponse> projectResponses = new ArrayList<>();
        for(Project project : projects){
            ProjectResponse projectResponse = mapper.map(project,ProjectResponse.class);
            projectResponses.add(projectResponse);
        }
        return projectResponses;
    }

    @Override
    public void updateProjectStatus(UUID projectId) {
        projectRepository.updateProjectStatus(projectId);
    }
}

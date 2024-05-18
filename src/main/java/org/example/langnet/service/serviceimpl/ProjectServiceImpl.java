package org.example.langnet.service.serviceimpl;

import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.dto.respond.UserInProjectResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.model.entity.Project;
import org.example.langnet.model.entity.Role;
import org.example.langnet.repository.AppUserRepository;
import org.example.langnet.repository.ProjectRepository;
import org.example.langnet.repository.RoleRepository;
import org.example.langnet.service.AppUserService;
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
        if (authentication == null) {
            throw new IllegalStateException("No authentication found in security context");
        }
        String email = authentication.getName();

        // Find the user by email
        AppUser user = appUserRepository.findUserByEmail(email);
        if (user == null) {
            throw new IllegalStateException("No user found with email: " + email);
        }

        // Get the role ID for "ProjectLeader"
        UUID roleId = roleRepository.getRoleByName("ProjectLeader");

        // Create a new project and set its name and users
        Project projectAfterInsert = projectRepository.createNewProject(projectCreationRequest);
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
            System.out.println(roleName);
            userInProjectResponse.setRole(roleName);
            userInProjectResponses.add(userInProjectResponse);
        }

        // Set the users in the project response
        projectResponse.setUsers(userInProjectResponses);

        // Insert the user into the project member table
        projectRepository.insertMemberIntoProjectMember(projectAfterInsert.getProjectId(), user.getUserId(), roleId);

        return projectResponse;
    }

    @Override
    public ProjectResponse getProjectById(UUID projectId) {
        return null;
    }
}

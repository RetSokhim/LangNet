package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.langnet.model.dto.request.ContactRequest;
import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.service.AppUserService;
import org.example.langnet.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/contact")
@SecurityRequirement(name = "bearerAuth")
public class ContactController {
    private final ContactService contactService;
    private final AppUserService appUserService;

    public ContactController(ContactService contactService, AppUserService appUserService) {
        this.contactService = contactService;
        this.appUserService = appUserService;
    }
    @PostMapping("/UpdateUserContact")
    @Operation(summary = "Add user's contact")
    public ResponseEntity<?> addContactToUser(@Valid @RequestBody ContactRequest contactRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.findUserByEmail(email);
        contactService.addContactToUser(contactRequest,user.getUserId());
        ApiResponse<ProjectResponse> apiResponse = new ApiResponse<>("Contact added successfully",
                HttpStatus.CREATED,null,201, LocalDateTime.now());
        return new ResponseEntity<>(apiResponse,HttpStatus.CREATED);
    }
}

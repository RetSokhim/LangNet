package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.example.langnet.exception.PasswordException;
import org.example.langnet.model.dto.request.AddMemberIntoProjectRequest;
import org.example.langnet.model.dto.request.ChangeUserPasswordRequest;
import org.example.langnet.model.dto.request.ProfileDetailRequest;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.UserProfileDetailResponse;
import org.example.langnet.model.dto.respond.UserResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/user")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    private final AppUserService appUserService;

    public UserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


    @GetMapping
    @Operation(summary = "Search user by username with project history")
    ResponseEntity<?> searchUserByName(@RequestParam String username){
        List<UserResponse> userResponse = appUserService.searchUserByName(username);
        return new ResponseEntity<>(new ApiResponse<>("User search successfully",
                HttpStatus.OK,
                userResponse,
                200,
                LocalDateTime.now()),HttpStatus.OK);
    }
    @PutMapping("/UpdateProfile")
    @Operation(summary = "Update user's profile")
    ResponseEntity<?> updateUserProfile(@Valid @RequestBody ProfileDetailRequest profileDetailRequest){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.findUserByEmail(email);
        appUserService.updateUserProfile(profileDetailRequest,user.getUserId());
        return new ResponseEntity<>(new ApiResponse<>("User profile updated successfully",
                HttpStatus.OK,
                null,
                200,
                LocalDateTime.now()),HttpStatus.OK);
    }
    @PutMapping("/ChangePassword")
    @Operation(summary = "Change password")
    ResponseEntity<?> changePassword(@Valid @RequestBody ChangeUserPasswordRequest changeUserPasswordRequest) throws PasswordException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        AppUser user = appUserService.findUserByEmail(email);
        appUserService.changePassword(changeUserPasswordRequest,user.getUserId());
        return new ResponseEntity<>(new ApiResponse<>("User's password changed successfully",
                HttpStatus.OK,
                null,
                200,
                LocalDateTime.now()),HttpStatus.OK);
    }
    @GetMapping("/GetProfileDetailWithUserName")
    @Operation(summary = "Get profile's detail with username")
    ResponseEntity<?> searchUserByUserName(@RequestParam String username){
        List<UserProfileDetailResponse> userProfileDetailResponse = appUserService.searchUserByUserName(username);
        return new ResponseEntity<>(new ApiResponse<>("Get user by username successfully",
                HttpStatus.OK,
                userProfileDetailResponse,
                200,
                LocalDateTime.now()),HttpStatus.OK);
    }
}

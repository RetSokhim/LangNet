package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.UserResponse;
import org.example.langnet.model.entity.Role;
import org.example.langnet.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/role")
@SecurityRequirement(name = "bearerAuth")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @GetMapping("/GetAllRole")
    @Operation(summary = "Get all role")
    ResponseEntity<?> searchUserByName(){
        List<Role> role = roleService.getAllRole();
        return new ResponseEntity<>(new ApiResponse<>("Get all role successfully",
                HttpStatus.OK,
                role,
                200,
                LocalDateTime.now()),HttpStatus.OK);
    }
}
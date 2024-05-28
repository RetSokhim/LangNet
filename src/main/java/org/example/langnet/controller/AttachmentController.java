package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.langnet.model.dto.request.AttachmentRequest;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.AttachmentResponse;
import org.example.langnet.model.dto.respond.ProjectResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.service.AttachmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/attachment")
@SecurityRequirement(name = "bearerAuth")
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload Attachment excel and insert into the database")
    public ResponseEntity<String> uploadAttachment(@ModelAttribute AttachmentRequest attachmentRequest) {
        try {
            attachmentService.uploadAttachment(attachmentRequest);
            return ResponseEntity.ok("Attachment uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to upload attachment: " + e.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<?> getAllProjectByCurrentUser() {
        List<AttachmentResponse> attachmentResponse = attachmentService.getAllAttachment();
        ApiResponse<List<AttachmentResponse>> apiResponse = new ApiResponse<>("Project deleted successfully",
                HttpStatus.OK,
                attachmentResponse, 200,
                LocalDateTime.now());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
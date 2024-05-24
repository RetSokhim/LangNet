package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.langnet.model.dto.request.AttachmentRequest;
import org.example.langnet.service.AttachmentService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/attachment")
@SecurityRequirement(name = "bearerAuth")
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
    @PostMapping(path = "/UploadExcelFileAndConvertIntoJSON", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = " Upload Attachment excel and convert into JSONB (Not done yet)")
    public ResponseEntity<String> uploadAttachmentFileConvertToJSON(@RequestParam("files") List<MultipartFile> files, @RequestBody AttachmentRequest attachmentRequest) {
        try {
            attachmentService.importFromExcelToJSONB(files,attachmentRequest);
            return ResponseEntity.ok("Files processed successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to process files: " + e.getMessage());
        }
    }
}

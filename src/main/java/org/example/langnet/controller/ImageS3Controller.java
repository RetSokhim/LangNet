package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.service.S3Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/image")
@SecurityRequirement(name = "bearerAuth")
public class ImageS3Controller {

    private final S3Service s3Service;

    public ImageS3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }
    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        ApiResponse<String> apiResponse = ApiResponse.<String>builder()
                .code(201)
                .message("Successfully uploaded file")
                .status(HttpStatus.CREATED)
                .payload(s3Service.uploadFile(file.getOriginalFilename(), file))
                .time(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(s3Service.getFile(fileName).getObjectContent()));
    }

    @GetMapping("/view/{fileName}")
    public ResponseEntity<InputStreamResource> viewFile(@PathVariable String fileName) {
        var s3Object = s3Service.getFile(fileName);
        var content = s3Object.getObjectContent();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .body(new InputStreamResource(content));
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        s3Service.deleteFile(filename);
        return ResponseEntity.ok("Delete");
    }
}

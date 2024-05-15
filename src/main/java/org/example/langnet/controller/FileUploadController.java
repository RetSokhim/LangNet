package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.langnet.model.dto.respond.ApiRespond;
import org.example.langnet.model.dto.respond.FileResponse;
import org.example.langnet.model.dto.respond.JsonbData;
import org.example.langnet.service.ExcelFileService;
import org.example.langnet.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/files")
public class FileUploadController {
    private final FileService fileService;
    private final ExcelFileService excelFileService;

    public FileUploadController(FileService fileService, ExcelFileService excelFileService) {
        this.fileService = fileService;
        this.excelFileService = excelFileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, name = "/upload")
    @Operation(summary = "Upload File")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String fileName = fileService.saveFile(file);
        FileResponse fileResponse = new FileResponse(fileName, file.getContentType(), file.getSize());
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }

    @GetMapping()
    @Operation(summary = "Get File")
    public ResponseEntity<?> getFile(@RequestParam String fileName) throws IOException {
        Resource resource = fileService.getFileByFileName(fileName);
        MediaType mediaType;
        if (fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
            mediaType = MediaType.IMAGE_PNG;
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType).body(resource);
    }
    @PostMapping(path = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadExcelFile(@RequestParam MultipartFile file) throws IOException {
        excelFileService.importFromExcel(file);
        return new ResponseEntity<>("Excel file processed successfully", HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/upload multi excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFiles(@RequestParam List<MultipartFile> files) {
        try {
            excelFileService.importFromExcelAsList(files);
            return ResponseEntity.ok("Files processed successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to process files: " + e.getMessage());
        }
    }

    @PostMapping(path = "/upload multi excel and convert to JSON", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFileConvertToJSON(@RequestParam("files") List<MultipartFile> files) {
        try {
            excelFileService.importFromExcelToJSONB(files);
            return ResponseEntity.ok("Files processed successfully.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Failed to process files: " + e.getMessage());
        }
    }

    @GetMapping("get all jsonb data")
    public ResponseEntity<?> getAllJsonbData(){
        return new ResponseEntity<>(new ApiRespond<>("Get all successful",HttpStatus.OK,excelFileService.getAllJsonData(),200, LocalDateTime.now()),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateExcelFile(@RequestParam String key, @RequestParam String newValue) {
            excelFileService.updateValueOfWord(key, newValue);
            return ResponseEntity.ok(excelFileService.getAllJsonData());
    }
}


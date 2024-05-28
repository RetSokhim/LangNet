package org.example.langnet.service.serviceimpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.example.langnet.model.dto.request.AttachmentDTO;
import org.example.langnet.model.dto.request.AttachmentRequest;
import org.example.langnet.model.dto.respond.AttachmentResponse;
import org.example.langnet.model.dto.respond.ExcelFileValue;
import org.example.langnet.repository.AttachmentRepository;
import org.example.langnet.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentImpl implements AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final ObjectMapper objectMapper;

    public AttachmentImpl(AttachmentRepository attachmentRepository, ObjectMapper objectMapper) {
        this.attachmentRepository = attachmentRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public void importFromExcelToJSONB(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            String json = convertExcelToJson(file);
            System.out.println(json);
//            attachmentRepository.insertJsonData(json);
        }
    }

    @Override
    public void uploadAttachment(AttachmentRequest attachmentRequest) throws IOException {
        String json = convertExcelToJson(attachmentRequest.getFile());

        AttachmentDTO updatedRequest = new AttachmentDTO(
                attachmentRequest.getBaseLanguage(),
                attachmentRequest.getLanguage(),
                attachmentRequest.getProject(),
                attachmentRequest.getPostedBy(),
                attachmentRequest.getAttachmentName(),
                json,
                attachmentRequest.getHints(),
                attachmentRequest.getPostedDate(),
                attachmentRequest.getExpireDate()
        );
        attachmentRepository.insertAttachment(updatedRequest);
    }

    @Override
    public List<AttachmentResponse> getAllAttachment() {
        return attachmentRepository.getAllAttachments();
    }

    private String getCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
    private String convertExcelToJson(MultipartFile file) throws IOException {
        List<ExcelFileValue> entities = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() > 0) { // Skip header row
                    ExcelFileValue entity = new ExcelFileValue();
                    entity.setId(UUID.randomUUID());
                    entity.setKey(getCellValue(row.getCell(0)));
                    entity.setValue(getCellValue(row.getCell(1)));
                    entities.add(entity);
                }
            }
        }
        return objectMapper.writeValueAsString(entities);
    }
}

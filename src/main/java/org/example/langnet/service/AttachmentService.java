package org.example.langnet.service;

import org.example.langnet.model.dto.request.AttachmentRequest;
import org.example.langnet.model.dto.respond.AttachmentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    void importFromExcelToJSONB(List<MultipartFile> file) throws IOException;
    void uploadAttachment(AttachmentRequest attachmentRequest) throws IOException;
    List<AttachmentResponse> getAllAttachment();
}

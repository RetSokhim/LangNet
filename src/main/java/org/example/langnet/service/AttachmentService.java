package org.example.langnet.service;

import org.example.langnet.model.dto.request.AttachmentRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
//    void importFromExcel(MultipartFile file) throws IOException;
//    void importFromExcelAsList(List<MultipartFile> files) throws IOException;
    void importFromExcelToJSONB(List<MultipartFile> files, AttachmentRequest attachmentRequest) throws IOException;
//    List<JsonbData> getAllJsonData();
//    void updateValueOfWord(String key,String value);
//    void updateValueById(UUID id,String newValue);

}

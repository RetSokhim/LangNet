package org.example.langnet.service;

import org.example.langnet.model.dto.request.ExcelFileRequest;
import org.example.langnet.model.dto.respond.JsonbData;
import org.example.langnet.model.entity.ExcelFileValue;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface ExcelFileService {
    void insertExcelIntoDatabase(ExcelFileValue excelFileValue);
    void importFromExcel(MultipartFile file) throws IOException;
    void importFromExcelAsList(List<MultipartFile> files) throws IOException;
    void importFromExcelToJSONB(List<MultipartFile> files) throws IOException;
    List<JsonbData> getAllJsonData();
    void updateValueOfWord(String key, String newValue);

}

package org.example.langnet.service;

import org.example.langnet.model.dto.request.ExcelFileRequest;
import org.example.langnet.model.entity.ExcelFileValue;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ExcelFileService {
    void insertExcelIntoDatabase(ExcelFileValue excelFileValue);
    void importFromExcel(MultipartFile file) throws IOException;
}

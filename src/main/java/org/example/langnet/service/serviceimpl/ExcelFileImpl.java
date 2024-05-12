package org.example.langnet.service.serviceimpl;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.example.langnet.model.entity.ExcelFileValue;
import org.example.langnet.repository.FileExcelRepository;
import org.example.langnet.service.ExcelFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ExcelFileImpl implements ExcelFileService {
    private final FileExcelRepository fileExcelRepository;

    @Override
    public void insertExcelIntoDatabase(ExcelFileValue excelFileValue) {
        fileExcelRepository.insertExcelIntoDatabase(excelFileValue);
    }

    @Override
    public void importFromExcel(MultipartFile file) throws IOException {
        List<ExcelFileValue> entities = convertExcelToEntities(file);

        for (ExcelFileValue entity : entities) {
            fileExcelRepository.insertExcelIntoDatabase(entity);
        }
    }

    private List<ExcelFileValue> convertExcelToEntities(MultipartFile file) throws IOException {
        List<ExcelFileValue> entities = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() > 0) { // Skip header row
                    ExcelFileValue entity = new ExcelFileValue();
                    entity.setKey(getCellValue(row.getCell(0)));
                    entity.setValue(getCellValue(row.getCell(1)));
                    entities.add(entity);
                }
            }
        }
        return entities;
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
}

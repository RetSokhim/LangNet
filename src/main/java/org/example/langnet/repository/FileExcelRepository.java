package org.example.langnet.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.langnet.model.dto.request.ExcelFileRequest;
import org.example.langnet.model.entity.ExcelFileValue;

@Mapper
public interface FileExcelRepository {
    @Select("""
    INSERT INTO excel_file_tb (key, value) VALUES (#{excel.key}, #{excel.value});
    """)
    void insertExcelIntoDatabase(@Param("excel") ExcelFileValue excelFileValue);
}

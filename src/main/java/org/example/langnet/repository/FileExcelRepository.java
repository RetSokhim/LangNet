package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.model.dto.respond.JsonbData;
import org.example.langnet.model.entity.ExcelFileValue;

import java.util.List;

@Mapper
public interface FileExcelRepository {
    @Insert("""
            INSERT INTO excel_file_tb (key, value) VALUES (#{excel.key}, #{excel.value});
            """)
    void insertExcelIntoDatabase(@Param("excel") ExcelFileValue excelFileValue);

    @Insert("""
            INSERT INTO excel_file_json_tb(data) VALUES (CAST(#{json} AS JSONB))
            """)
    void insertJsonData(String json);

    @Select("""
            SELECT
            (jsonb_array_elements(data)->>'id')::UUID AS id,
            jsonb_array_elements(data)->>'key' AS key,
            jsonb_array_elements(data)->>'value' AS value
            FROM
            excel_file_json_tb;
                                    """)
    List<JsonbData> getAllJsonData();

    @Update("""
            UPDATE excel_file_json_tb
            SET data = (
                SELECT jsonb_agg(
                    CASE
                        WHEN elem->>'key' = #{key} THEN jsonb_set(elem, '{value}', to_jsonb(#{newValue}::text), false)
                        ELSE elem
                    END
                )
                FROM jsonb_array_elements(data) AS elem
            )
            WHERE data @> jsonb_build_array(jsonb_build_object('key', #{key}));
            """)
    void updateValueOfWord(@Param("key") String key, @Param("newValue") String newValue);
}

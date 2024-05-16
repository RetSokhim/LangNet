package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;

@Mapper
public interface AttachmentRepository {

    @Insert("""
            INSERT INTO attachment_tb(data) VALUES (CAST(#{json} AS JSONB))
            """)
    void insertJsonData(String json);

//    @Select("""
//            SELECT
//            (jsonb_array_elements(data)->>'id')::UUID AS id,
//            jsonb_array_elements(data)->>'key' AS key,
//            jsonb_array_elements(data)->>'value' AS value
//            FROM
//            excel_file_json_tb;
//                                    """)
//    List<JsonbData> getAllJsonData();

//    @Update("""
//            UPDATE excel_file_json_tb
//            SET data = (
//                SELECT jsonb_agg(
//                    CASE
//                        WHEN elem->>'key' = #{key} THEN jsonb_set(elem, '{value}', to_jsonb(#{newValue}::text), false)
//                        ELSE elem
//                    END
//                )
//                FROM jsonb_array_elements(data) AS elem
//            )
//            WHERE data @> jsonb_build_array(jsonb_build_object('key', #{key}));
//            """)
//    void updateValueOfWord(@Param("key") String key, @Param("newValue") String newValue);
//
//    @Update("""
//    WITH updated_data AS (
//        SELECT
//            efjt.id,
//            jsonb_agg(
//                CASE
//                    WHEN obj->>'id' = #{id}::text THEN jsonb_set(obj, '{value}', to_jsonb(#{newValue}::text), true)
//                    ELSE obj
//                END
//            ) AS updated_json
//        FROM
//            excel_file_json_tb efjt,
//            jsonb_array_elements(efjt.data) AS obj
//        WHERE
//            efjt.id = #{id}
//        GROUP BY
//            efjt.id
//    )
//    UPDATE excel_file_json_tb
//    SET data = ud.updated_json
//    FROM updated_data ud
//    WHERE excel_file_json_tb.id = ud.id;
//    """)
//    void updateValueById(@Param("id") UUID id, @Param("newValue") String newValue);
}

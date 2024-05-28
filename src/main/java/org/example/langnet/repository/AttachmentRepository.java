package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.model.dto.request.AttachmentDTO;
import org.example.langnet.model.dto.request.AttachmentRequest;
import org.example.langnet.model.dto.respond.AttachmentResponse;

import java.util.List;

@Mapper
public interface AttachmentRepository {
    @Insert("""
        INSERT INTO attachment_tb(project_id,base_language, language_id, posted_by, attachment_name, data, expire_date)
        VALUES (#{project},#{baseLanguage}, #{language}, #{postedBy}, #{attachmentName}, CAST(#{data} AS JSONB),#{expireDate})
        """)
    void insertAttachment(AttachmentDTO attachmentRequest);

    @Select("""
    SELECT
        at.attachment_id AS attachmentId,
        lt.language_id AS languageId,
        lt.language AS language,
        at.base_language AS baseLanguage,
        lt.language_code AS languageCode,
        ut.user_id AS userId,
        ut.username AS username,
        ut.email AS email,
        at.attachment_name AS attachmentName,
        at.data AS data,
        at.status AS status,
        at.posted_date AS postedDate,
        at.expire_date AS expireDate
    FROM
        attachment_tb at
    JOIN
        language_tb lt ON at.language_id = lt.language_id
    JOIN
        user_tb ut ON at.posted_by = ut.user_id
    """)
    @Results(id = "attachmentResultMap", value = {
            @Result(property = "attachmentId", column = "attachmentId"),
            @Result(property = "language.languageId", column = "languageId"),
            @Result(property = "language.language", column = "language"),
            @Result(property = "language.languageCode", column = "languageCode"),
            @Result(property = "postBy.userId", column = "userId"),
            @Result(property = "postBy.username", column = "username"),
            @Result(property = "postBy.email", column = "email"),
            @Result(property = "attachmentName", column = "attachmentName"),
            @Result(property = "data", column = "data"),
            @Result(property = "status", column = "status"),
            @Result(property = "postedDate", column = "postedDate"),
            @Result(property = "expireDate", column = "expireDate"),
            @Result(property = "baseLanguage", column = "base_Language"),
//            @Result(property = "feedback", column = "attachmentId", many = @Many(select = "getFeedbackByAttachmentId")),
//            @Result(property = "hints", column = "attachmentId", many = @Many(select = "getHintsByAttachmentId"))
    })
    List<AttachmentResponse> getAllAttachments();


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

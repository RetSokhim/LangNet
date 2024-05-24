package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.model.dto.request.ContactRequest;
import org.example.langnet.model.entity.Contact;

import java.util.UUID;

@Mapper
public interface ContactRepository {

    @Select("""
    SELECT * FROM contact_tb WHERE user_id = #{userId}::UUID
    """)
    @Results(id = "contactMapping",value = {
            @Result(property = "contactId",column = "contact_id"),
            @Result(property = "phoneNumber",column = "phone_number")
    })
    Contact getContactByUserId(UUID userId);

    @Insert("""
    INSERT INTO contact_tb(facebook, phone_number, telegram, user_id)
    VALUES (#{contact.facebook},#{contact.phoneNumber},#{contact.telegram},#{userId}::UUID)
    """)
    void addContactToUser(@Param("contact") ContactRequest contactRequest, UUID userId);
}

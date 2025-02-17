package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.model.entity.Role;

import java.util.List;
import java.util.UUID;

@Mapper
public interface RoleRepository {
    @Select("""
               SELECT role_tb.role_name FROM role_tb
               JOIN public.project_member_tb pmt on role_tb.role_id = pmt.role_id
               WHERE user_id = #{userId}
            """)
    List<String> getRoleNamesByUserId(UUID userId);

    @Select("""
               SELECT role_id FROM role_tb WHERE role_name = #{roleName}
            """)
    UUID getRoleByName(String roleName);

    @Select("""
               SELECT role_tb.role_name FROM role_tb
               JOIN project_member_tb pmt on role_tb.role_id = pmt.role_id
               WHERE pmt.user_id = #{userId}::UUID AND pmt.project_id = #{projectId}::UUID
            """)
    String getRoleNameByUserIdAndProjectId(UUID userId,UUID projectId);

    @Select("""
    SELECT * FROM role_tb
    """)
    @Results(id = "roleMapping",value = {
            @Result(property = "roleId",column = "role_id"),
            @Result(property = "roleName",column = "role_name")
    })
    List<Role> getAllRole();
}

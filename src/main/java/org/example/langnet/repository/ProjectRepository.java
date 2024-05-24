package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.configuration.UUIDTypeHandler;
import org.example.langnet.model.dto.request.ProjectCreationRequest;
import org.example.langnet.model.entity.Project;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ProjectRepository {

    @Select("""
    INSERT INTO project_tb(project_name) VALUES (#{project.projectName}) RETURNING *
    """)
    @Results(id = "projectMapping",value = {
            @Result(property = "projectId",column = "project_id",typeHandler = UUIDTypeHandler.class),
            @Result(property = "projectName",column = "project_name"),
            @Result(property = "createDate",column = "create_date"),
            @Result(property = "updatedDate",column = "updated_date"),
            @Result(property = "removedDate",column = "removed_date"),
            @Result(property = "users",column = "project_id",
            many = @Many(select = "org.example.langnet.repository.AppUserRepository.getMemberByProjectIdInProject")
            )

    })
    Project createNewProject(@Param("project") ProjectCreationRequest projectCreationRequest);

    @Insert("""
    INSERT INTO project_member_tb(project_id, user_id, role_id) VALUES (#{projectId}::UUID,#{userId}::UUID,#{roleId}::UUID)
    """)
    void insertMemberIntoProjectMember(UUID projectId, UUID userId, UUID roleId);

    @Select("""
    SELECT * FROM project_tb WHERE project_id = #{projectId}::UUID
    """)
    @ResultMap("projectMapping")
    Project getProjectById(UUID projectId);

    @Update("""
    UPDATE project_tb
    SET active = false
    WHERE project_id = #{projectId}
    """)
    void deleteProjectIdByUpdateActive(UUID projectId);
    @Select("""
        SELECT *
        FROM project_tb p
        INNER JOIN project_member_tb pm ON p.project_id = pm.project_id
        INNER JOIN user_tb u ON pm.user_id = u.user_id
        INNER JOIN role_tb r ON pm.role_id = r.role_id
        WHERE p.project_name ILIKE CONCAT('%', #{projectName}, '%') AND u.user_id = #{userId}::UUID
    """)
    @Results(value = {
            @Result(property = "projectId",column = "project_id",typeHandler = UUIDTypeHandler.class),
            @Result(property = "projectName",column = "project_name"),
            @Result(property = "createDate",column = "create_date"),
            @Result(property = "updatedDate",column = "updated_date"),
            @Result(property = "removedDate",column = "removed_date"),
            @Result(property = "users", javaType = List.class, column = "project_id",
                    many = @Many(select = "org.example.langnet.repository.AppUserRepository.getMemberByProjectIdInProject"))
    })
    List<Project> searchProjectByName(String projectName,UUID userId);

    @Select("""
    SELECT * FROM project_tb
    JOIN public.project_member_tb pmt on project_tb.project_id = pmt.project_id
    WHERE user_id = #{userId}::UUID
    """)
    @ResultMap("projectMapping")
    Project getProjectByUserId(UUID userId);

    @Select("""
    SELECT * FROM project_tb
    JOIN public.project_member_tb pmt on project_tb.project_id = pmt.project_id
    WHERE user_id = #{userId}::UUID
    """)
    @ResultMap("projectMapping")
    List<Project> getAllProjectByCurrentUser(UUID userId);

    void updateProjectStatus(UUID projectId);
}

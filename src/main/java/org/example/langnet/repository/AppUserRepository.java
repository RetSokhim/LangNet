package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.configuration.UUIDTypeHandler;
import org.example.langnet.model.dto.request.AddMemberIntoProjectRequest;
import org.example.langnet.model.dto.request.ChangeUserPasswordRequest;
import org.example.langnet.model.dto.request.ProfileDetailRequest;
import org.example.langnet.model.dto.respond.UserInProjectResponse;
import org.example.langnet.model.entity.AppUser;

import java.util.List;
import java.util.UUID;

@Mapper
public interface AppUserRepository {

    @Select("""
    SELECT * FROM user_tb WHERE email = #{username} OR username = #{username};
    """)
    @Results(id = "userMapping", value = {
            @Result(property = "userId", column = "user_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "birthDate", column = "birth_date"),
            @Result(property = "phoneNumber", column = "phone_number"),
            @Result(property = "contact",column = "user_id",
            one = @One(select = "org.example.langnet.repository.ContactRepository.getContactByUserId")
            ),
            @Result(property = "projects",column = "user_id",
            many = @Many(select = "org.example.langnet.repository.ProjectRepository.getProjectByUserId")
            )
    })
    AppUser findUserByEmail(String username);

    @Select("""
    INSERT INTO user_tb(username,firstname,lastname,email,gender,birth_date,password)
    VALUES (#{user.username},#{user.firstName},#{user.lastName},#{user.email},#{user.gender},#{user.birthDate},#{user.password}) RETURNING *
    """)
    @ResultMap("userMapping")
    AppUser registerNewUser(@Param("user") AppUser appUser);

    @Select("""
    SELECT * FROM user_tb WHERE user_id = #{userId}
    """)
    @ResultMap("userMapping")
    AppUser getUserById(UUID userId);

    @Update("""
    UPDATE user_tb
    SET password = #{password}
    WHERE user_id = #{userId}::UUID
    """)
    void updatePassword(@Param("userId") UUID userId, @Param("password") String password);

    @Select("""
    SELECT u.user_id, u.username, u.email, u.firstname, u.lastname, u.birth_date, u.image, u.gender,
           c.contact_id, c.phone_number, c.facebook, c.telegram,
           r.role_name
    FROM user_tb u
    JOIN project_member_tb pm ON u.user_id = pm.user_id
    JOIN role_tb r ON pm.role_id = r.role_id
    LEFT JOIN contact_tb c ON u.user_id = c.user_id
    WHERE pm.project_id = #{projectId}
""")
    @Results(id = "userResponseMapping",value = {
            @Result(property = "userId", column = "user_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "firstName", column = "firstname"),
            @Result(property = "lastName", column = "lastname"),
            @Result(property = "birthDate", column = "birth_date"),
            @Result(property = "contact.contactId",column = "contact_id"),
            @Result(property = "contact.phoneNumber", column = "phone_number"),
            @Result(property = "contact.facebook", column = "facebook"),
            @Result(property = "contact.telegram", column = "telegram"),
            @Result(property = "role", column = "role_name")
    })
    List<UserInProjectResponse> getMemberByProjectIdInProject(UUID projectId);

    @Insert("""
    INSERT INTO user_tb(username, email, password,image)
    VALUES (#{user.username},#{user.email},#{user.password},#{user.image})
    """)
    void registerNewUserLoginByThirdParty(@Param("user") AppUser user);
    @Select("SELECT EXISTS(SELECT 1 FROM user_tb WHERE email = #{email})")
    Boolean selectExistUser(String email);

    @Select("""
    SELECT u.user_id, u.username, u.email, r.role_id, r.role_name
    FROM user_tb u
    JOIN project_member_tb pm ON u.user_id = pm.user_id
    JOIN role_tb r ON pm.role_id = r.role_id
    WHERE pm.project_id = #{projectId}
    """)
    @Results(value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "roles", column = "user_id", javaType = List.class,
                    many = @Many(select = "org.example.langnet.repository.RoleRepository.getRoleNamesByUserId"))
    })
    List<AppUser> getUsersByProjectId(UUID projectId);

    @Select("""
    SELECT * FROM user_tb WHERE username ILIKE CONCAT('%', #{username}, '%')
    """)
    @ResultMap("userMapping")
    List<AppUser> searchUserByUserName(String username);

    @Insert("""
    INSERT INTO project_member_tb(project_id, user_id, role_id)
    VALUES (#{member.projectId},#{member.userId},#{member.roleId})
    """)
    void addNewMemberToProject(@Param("member") AddMemberIntoProjectRequest addMemberIntoProjectRequest);

    @Select("""
    UPDATE user_tb
    SET username = #{profile.username},firstname = #{profile.firstName},lastname = #{profile.lastName},gender = #{profile.gender},birth_date = #{profile.birthDate}
    WHERE user_id = #{userId}::UUID
    """)
    void updateUserProfile(@Param("profile") ProfileDetailRequest profileDetailRequest, UUID userId);

    @Select("""
    UPDATE user_tb
    SET password = #{password.newPassword}
    WHERE user_id = #{userId}::UUID
    """)
    void changePassword(@Param("password") ChangeUserPasswordRequest changeUserPasswordRequest, UUID userId);

    @Select("""
    SELECT * FROM user_tb WHERE username ILIKE CONCAT('%', #{username}, '%')
    """)
    @ResultMap("userMapping")
    List<AppUser> searchUserProfileByUserName(String username);
}

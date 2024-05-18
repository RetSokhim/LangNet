package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.configuration.UUIDTypeHandler;
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
            @Result(property = "phoneNumber", column = "phone_number")
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
    SELECT * FROM user_tb
    JOIN public.project_member_tb pmt on user_tb.user_id = pmt.user_id
    WHERE project_id = #{projectId}::UUID
    """)
    @ResultMap("userMapping")
    List<AppUser> getMemberByProjectIdInProject(UUID projectId);

    @Insert("""
    INSERT INTO user_tb(username, email, password,image)
    VALUES (#{user.username},#{user.email},#{user.password},#{user.image})
    """)
    void registerNewUserLoginByThirdParty(@Param("user") AppUser user);
    @Select("SELECT EXISTS(SELECT 1 FROM user_tb WHERE email = #{email})")
    Boolean selectExistUser(String email);
}

package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.configuration.UUIDTypeHandler;
import org.example.langnet.model.entity.AppUser;

import java.util.UUID;

@Mapper
public interface AppUserRepository {

    @Select("""
    SELECT * FROM user_tb WHERE email = #{username} OR username = #{username};
    """)
    @Results(id = "userMapping",value = {
            @Result(property = "userId",column = "user_id",typeHandler = UUIDTypeHandler.class),
            @Result(property = "firstName",column = "firstname"),
            @Result(property = "lastName",column = "lastname"),
            @Result(property = "birthDate",column = "birth_date"),
            @Result(property = "phoneNumber",column = "phone_number")
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
}

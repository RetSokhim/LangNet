package org.example.langnet.repository;

import org.apache.ibatis.annotations.*;
import org.example.langnet.configuration.UUIDTypeHandler;
import org.example.langnet.model.dto.request.OtpsRequestDTO;
import org.example.langnet.model.entity.Otps;

import java.util.UUID;

@Mapper
public interface OtpsRepository {
    @Insert("""
            INSERT INTO otps_tb(otps_code, issued_at, expiration_date, user_id)
            VALUES (#{otp.otpsCode},#{otp.issuedAt},#{otp.expirationDate},#{otp.user})
            """)
    void insertOtpAfterSent(@Param("otp") OtpsRequestDTO otp);

    @Select("""
            SELECT * FROM otps_tb WHERE user_id = #{userId}::UUID ORDER BY issued_at LIMIT 1
            """)
    @Results(id = "otpMapping", value = {
            @Result(property = "otpsId", column = "otps_id", typeHandler = UUIDTypeHandler.class),
            @Result(property = "otpsCode", column = "otps_code"),
            @Result(property = "issuedAt", column = "issued_at"),
            @Result(property = "expirationDate", column = "expiration_date"),
            @Result(property = "user", column = "user_id",
                    one = @One(select = "org.example.langnet.repository.AppUserRepository.getUserById")
            )
    })
    Otps getOtpsByUserId(UUID userId);

    @Select("""
            SELECT * FROM otps_tb WHERE otps_code = #{otp}
            """)
    @ResultMap("otpMapping")
    Otps getOtpsByOtpCode(Long otp);

    @Update("""
            UPDATE otps_tb
            SET verify = #{otps.verify}
            WHERE otps_code = #{otps.otpsCode}
            """)
    void confirmVerify(@Param("otps") Otps otps);

    @Update("""
            UPDATE otps_tb
            SET otps_code = #{otps.otpsCode},issued_at = #{otps.issuedAt},expiration_date = #{otps.expirationDate}
            WHERE user_id = #{userId}::UUID
            """)
    void updateTheCodeAfterResend(@Param("otps") OtpsRequestDTO otps, UUID userId);

    @Update("""
            UPDATE otps_tb
            SET verify = true
            WHERE user_id = #{userId}::UUID
            """)
    void confirmVerifyByUserId(UUID userId);

    @Update("""
    UPDATE otps_tb
    SET otps_code = #{otp.otpsCode}, issued_at = #{otp.issuedAt}, expiration_date = #{otp.expirationDate} , verify = true
    WHERE user_id = #{otp.user}
    """)
    void updateOTPToResetPassword(@Param("otp") OtpsRequestDTO otpsRequestDTO);
}

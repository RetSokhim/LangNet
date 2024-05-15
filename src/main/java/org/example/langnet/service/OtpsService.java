package org.example.langnet.service;

import org.example.langnet.model.dto.request.OtpsRequestDTO;
import org.example.langnet.model.entity.Otps;

import java.util.UUID;

public interface OtpsService {
    OtpsRequestDTO generateOtp ();
    void insertOtpAfterSent(OtpsRequestDTO otps);
    Otps getOtpsByUserId(UUID userId);
    Otps getOtpsByOtpCode(Long otp);
    void confirmVerify(Otps otps);
    void updateTheCodeAfterResend(OtpsRequestDTO otps, UUID userId);
    boolean verifyOtp(Long otp, UUID userId) throws Exception;
    void confirmVerifyByUserId(UUID userId);
    void updateOTPToResetPassword(OtpsRequestDTO otpsRequestDTO);
}

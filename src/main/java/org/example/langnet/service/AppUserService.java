package org.example.langnet.service;

import org.example.langnet.exception.AccountVerificationException;
import org.example.langnet.exception.OTPExpiredException;
import org.example.langnet.model.dto.request.UserPasswordRequest;
import org.example.langnet.model.dto.request.UserRegisterRequest;
import org.example.langnet.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AppUserService extends UserDetailsService {
    AppUser findUserByEmail(String username);
    void registerNewUser(UserRegisterRequest userRegisterRequest) throws Exception;
    void verifyAccount(Long otp) throws AccountVerificationException, OTPExpiredException;
    void resendOtpCode(String email) throws Exception;
    void sendPasswordResetOtp(String email) throws Exception;
    void verifyAndResetPassword(UUID userId,Long otpCode, String newPassword) throws OTPExpiredException;
    void updatePassword(UUID userId,String password);
    void resetPassword(UserPasswordRequest userPasswordRequest, String email);
}

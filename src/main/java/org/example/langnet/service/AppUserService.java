package org.example.langnet.service;

import org.example.langnet.exception.AccountVerificationException;
import org.example.langnet.exception.OTPExpiredException;
import org.example.langnet.exception.PasswordException;
import org.example.langnet.model.dto.request.*;
import org.example.langnet.model.dto.respond.UserProfileDetailResponse;
import org.example.langnet.model.dto.respond.UserResponse;
import org.example.langnet.model.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface AppUserService extends UserDetailsService {
    AppUser findUserByEmail(String username);
    void registerNewUser(UserRegisterRequest userRegisterRequest) throws Exception;
    void verifyAccount(Long otp) throws AccountVerificationException, OTPExpiredException;
    void resendOtpCode(String email) throws Exception;
    void sendPasswordResetOtp(String email) throws Exception;
    void verifyAndResetPassword(UUID userId,Long otpCode, String newPassword) throws OTPExpiredException;
    void updatePassword(UUID userId,String password);
    void resetPassword(UserPasswordRequest userPasswordRequest, UUID userId);
    void registerNewUserFromThirdParty(LoginWithThirdPartyRequest loginWithThirdPartyRequest);
    Boolean selectExistUser(String email);
    void addNewMemberToProject(AddMemberIntoProjectRequest addMemberIntoProjectRequest);
    List<UserResponse> searchUserByName(String username);
    void updateUserProfile(ProfileDetailRequest profileDetailRequest, UUID userId);
    void changePassword(ChangeUserPasswordRequest changeUserPasswordRequest, UUID userId) throws PasswordException;
    List<UserProfileDetailResponse> searchUserByUserName(String username);
}

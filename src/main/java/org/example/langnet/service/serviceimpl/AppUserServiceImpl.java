package org.example.langnet.service.serviceimpl;

import lombok.AllArgsConstructor;
import org.example.langnet.exception.AccountVerificationException;
import org.example.langnet.exception.OTPExpiredException;
import org.example.langnet.exception.SearchNotFoundException;
import org.example.langnet.model.CustomAppUserDetail;
import org.example.langnet.model.dto.request.LoginWithThirdPartyRequest;
import org.example.langnet.model.dto.request.UserPasswordRequest;
import org.example.langnet.model.dto.request.UserRegisterRequest;
import org.example.langnet.model.dto.request.OtpsRequestDTO;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.model.entity.Otps;
import org.example.langnet.repository.AppUserRepository;
import org.example.langnet.service.AppUserService;
import org.example.langnet.service.EmailService;
import org.example.langnet.service.OtpsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final OtpsService otpsService;
    private final EmailService emailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, OtpsService otpsService, EmailService emailService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.otpsService = otpsService;
        this.emailService = emailService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //For authenticate user from database
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findUserByEmail(username);
        return new CustomAppUserDetail(user);
    }

    @Override
    public AppUser findUserByEmail(String username) {
        return appUserRepository.findUserByEmail(username);
    }

    @Override
    public void registerNewUser(UserRegisterRequest userRegisterRequest) throws Exception {
        OtpsRequestDTO otps = otpsService.generateOtp();

        Context context = new Context();
        context.setVariable("message", String.valueOf(otps.getOtpsCode()));
        emailService.sendEmailWithHtmlTemplate(userRegisterRequest.getEmail(),
                "Here is your OTP code to verify",
                "email-template", context);
        String password = bCryptPasswordEncoder.encode(userRegisterRequest.getPassword());
        userRegisterRequest.setPassword(password);

        AppUser user = new AppUser();
        user.setUsername(userRegisterRequest.getFirstName() + " " + userRegisterRequest.getLastName());
        user.setFirstName(userRegisterRequest.getFirstName());
        user.setLastName(userRegisterRequest.getLastName());
        user.setBirthDate(userRegisterRequest.getBirthDate());
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());
        user.setGender(userRegisterRequest.getGender());
        AppUser userAfterInsertion = appUserRepository.registerNewUser(user);
        otps.setUser(userAfterInsertion.getUserId());
        otpsService.insertOtpAfterSent(otps);
    }

    @Override
    public void verifyAccount(Long otp) throws AccountVerificationException, OTPExpiredException {
        Otps otps = otpsService.getOtpsByOtpCode(otp);
        if (otps != null) {
            if (Timestamp.valueOf(otps.getExpirationDate()).before(new Timestamp(System.currentTimeMillis()))) {
                throw new OTPExpiredException("OTP has expired.");
            }
            if (!otps.getVerify()) {
                otps.setVerify(true);
                otpsService.confirmVerify(otps);
            } else {
                throw new AccountVerificationException("Your account has already been verified");
            }
        } else {
            throw new OTPExpiredException("Invalid OTP code");
        }
    }

    @Override
    public void resendOtpCode(String email) throws Exception {
        AppUser user = appUserRepository.findUserByEmail(email);
        if (user == null) {
            throw new SearchNotFoundException("Cannot find your email account please register first");
        }
        Otps otp = otpsService.getOtpsByUserId(user.getUserId());
        if (otp.getVerify()) {
            throw new AccountVerificationException("Your account is already verified");
        }
        OtpsRequestDTO otps = otpsService.generateOtp();
        Context context = new Context();
        context.setVariable("message", String.valueOf(otps.getOtpsCode()));
        emailService.sendEmailWithHtmlTemplate(email,
                "Here is your OTP code to verify",
                "email-template", context);
        otpsService.updateTheCodeAfterResend(otps, user.getUserId());
    }

    //For send OTP to reset password
    @Override
    public void sendPasswordResetOtp(String email) throws Exception {
        AppUser user = appUserRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        OtpsRequestDTO otpsRequestDTO = otpsService.generateOtp();
        otpsRequestDTO.setUser(user.getUserId());
        otpsService.updateOTPToResetPassword(otpsRequestDTO);
        Context context = new Context();
        context.setVariable("message", String.valueOf(otpsRequestDTO.getOtpsCode()));
        emailService.sendEmailWithHtmlTemplate(email,
                "Here is your OTP code to verify",
                "email-template", context);
    }

    @Override
    public void verifyAndResetPassword(UUID userId, Long otpCode, String newPassword) throws OTPExpiredException {
        Otps otp = otpsService.getOtpsByOtpCode(otpCode);
        if (otp == null || !otp.getOtpsCode().equals(otpCode) || Timestamp.valueOf(otp.getExpirationDate()).before(new Timestamp(System.currentTimeMillis()))) {
            throw new OTPExpiredException("OTP is invalid or has expired.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        appUserRepository.updatePassword(userId, encodedPassword);
    }

    @Override
    public void updatePassword(UUID userId, String password) {
        appUserRepository.updatePassword(userId, password);
    }

    @Override
    public void resetPassword(UserPasswordRequest userPasswordRequest, String email) {
        AppUser user = appUserRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        otpsService.confirmVerifyByUserId(user.getUserId());
        String encodedPassword = bCryptPasswordEncoder.encode(userPasswordRequest.getPassword());
        appUserRepository.updatePassword(user.getUserId(), encodedPassword);
    }

    @Override
    public void registerNewUserFromThirdParty(LoginWithThirdPartyRequest request) {
        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword())); // Encode the password
        user.setImage(request.getImage());
        System.out.println(user.toString());
        appUserRepository.registerNewUserLoginByThirdParty(user);
    }

    @Override
    public Boolean selectExistUser(String email) {
        return appUserRepository.selectExistUser(email);
    }
}

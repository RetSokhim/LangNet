package org.example.langnet.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.example.langnet.exception.AccountVerificationException;
import org.example.langnet.exception.OTPExpiredException;
import org.example.langnet.exception.PasswordException;
import org.example.langnet.exception.SearchNotFoundException;
import org.example.langnet.model.CustomAppUserDetail;
import org.example.langnet.model.dto.request.*;
import org.example.langnet.model.dto.respond.ApiResponse;
import org.example.langnet.model.dto.respond.UserLoginTokenResponse;
import org.example.langnet.model.entity.AppUser;
import org.example.langnet.model.entity.Otps;
import org.example.langnet.security.JwtService;
import org.example.langnet.service.AppUserService;
import org.example.langnet.service.OtpsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AppUserService appUserService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final OtpsService otpsService;

    public AuthController(AppUserService appUserService, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, OtpsService otpsService) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.otpsService = otpsService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest userLoginRequest) throws Exception {
        if(appUserService.findUserByEmail(userLoginRequest.getEmail())==null){
            throw new SearchNotFoundException("User with this email is not found");
        }
        authenticate(userLoginRequest.getEmail(), userLoginRequest.getPassword());
        UserDetails userDetails = appUserService.loadUserByUsername(userLoginRequest.getEmail());
        AppUser user = ((CustomAppUserDetail) userDetails).getUser();
        Otps otps = otpsService.getOtpsByUserId(user.getUserId());
        if (otps == null || !otps.getVerify()) {
            throw new AccountVerificationException("Please verify your account first");
        }
        final String token = jwtService.generateToken(userDetails);
        UserLoginTokenResponse authResponse = new UserLoginTokenResponse(token);
        return ResponseEntity.ok(authResponse);
    }
    private void authenticate(String email, String password) throws Exception {
        try {
            UserDetails user = appUserService.loadUserByUsername(email);
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new PasswordException("Your password is incorrect please try again");
            }
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) throws Exception {
        appUserService.registerNewUser(userRegisterRequest);
        return new ResponseEntity<>(new ApiResponse<>("Register successfully", HttpStatus.CREATED, null, 201, LocalDateTime.now()), HttpStatus.CREATED);
    }

    //For verify account after registered
    @PutMapping("/verify")
    @Operation(summary = "Verify account before login")
    public ResponseEntity<?> verify(@RequestParam Long otp) throws AccountVerificationException, OTPExpiredException {
        appUserService.verifyAccount(otp);
        return new ResponseEntity<>("Your account is successfully verified", HttpStatus.OK);
    }

    //For resend OTP when expired or forget
    @PostMapping("/resend")
    @Operation(summary = "Resend OTP after expire or invalid")
    public ResponseEntity<?> resendOtpCode(@RequestParam String email) throws Exception {
        appUserService.resendOtpCode(email);
        return new ResponseEntity<>("Your new verification code has already resent", HttpStatus.OK);
    }

    //For send OTP when forget password
    @PostMapping("/reset-password/request")
    @Operation(summary = "Request OTP to reset password")
    public ResponseEntity<?> requestPasswordResetOtp(@RequestParam String email) {
        try {
            appUserService.sendPasswordResetOtp(email);
            return ResponseEntity.ok("OTP sent to your email for password reset.");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //For reset password when forget
    @PutMapping("/reset-password")
    @Operation(summary = "Verify OTP to reset password")
    public ResponseEntity<?> resetPassword(@RequestParam Long otp,
                                           @Valid @RequestBody UserPasswordRequest userPasswordRequest) {
        try {
            UUID userId = otpsService.getOtpsByOtpCode(otp).getUser().getUserId();
            boolean isVerified = otpsService.verifyOtp(otp, userId);

            if (isVerified) {
                appUserService.resetPassword(userPasswordRequest,userId);
                return ResponseEntity.ok("Your password has been successfully reset.");
            } else {
                return new ResponseEntity<>("Invalid OTP or expired.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/LoginWith3rdParty")
    @Operation(summary = "Login with third party such as Google and Github")
    public ResponseEntity<?> loginWithThirdParty(@Valid @RequestBody LoginWithThirdPartyRequest loginWithThirdPartyRequest) throws Exception {
        System.out.println(loginWithThirdPartyRequest.toString());
        boolean isUserExist = appUserService.selectExistUser(loginWithThirdPartyRequest.getEmail());
        if(isUserExist){
            authenticate(loginWithThirdPartyRequest.getEmail(), loginWithThirdPartyRequest.getPassword());
            UserDetails userDetails = appUserService.loadUserByUsername(loginWithThirdPartyRequest.getEmail());
            final String token = jwtService.generateToken(userDetails);
            UserLoginTokenResponse authResponse = new UserLoginTokenResponse(token);
            return ResponseEntity.ok(authResponse);
        }else {
            appUserService.registerNewUserFromThirdParty(loginWithThirdPartyRequest);
            authenticate(loginWithThirdPartyRequest.getEmail(), loginWithThirdPartyRequest.getPassword());
            UserDetails userDetails = appUserService.loadUserByUsername(loginWithThirdPartyRequest.getEmail());
            final String token = jwtService.generateToken(userDetails);
            UserLoginTokenResponse authResponse = new UserLoginTokenResponse(token);
            return ResponseEntity.ok(authResponse);
        }
    }
}

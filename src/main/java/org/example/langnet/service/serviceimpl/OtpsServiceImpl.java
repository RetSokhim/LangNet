package org.example.langnet.service.serviceimpl;

import lombok.AllArgsConstructor;
import org.example.langnet.model.dto.request.OtpsRequestDTO;
import org.example.langnet.model.entity.Otps;
import org.example.langnet.repository.OtpsRepository;
import org.example.langnet.service.OtpsService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OtpsServiceImpl implements OtpsService {
    private final OtpsRepository otpsRepository;
    @Override
    public OtpsRequestDTO generateOtp() {
        OtpsRequestDTO otps = new OtpsRequestDTO();
        Random random = new Random();
        int randomNum = random.nextInt(999999);
        StringBuilder otp = new StringBuilder(Integer.toString(randomNum));
        while (otp.length() < 6) {
            otp.insert(0, "0");
        }
        otps.setIssuedAt(LocalDateTime.now());
        Long sentOtp = Long.parseLong(otp.toString());
        otps.setOtpsCode(sentOtp);
        otps.setExpirationDate(LocalDateTime.now().plusMinutes(5));
        return otps;
    }

    @Override
    public void insertOtpAfterSent(OtpsRequestDTO otps) {
        otpsRepository.insertOtpAfterSent(otps);
    }
    @Override
    public Otps getOtpsByUserId(UUID userId) {
        return otpsRepository.getOtpsByUserId(userId);
    }

    @Override
    public Otps getOtpsByOtpCode(Long otp) {
        return otpsRepository.getOtpsByOtpCode(otp);
    }

    @Override
    public void confirmVerify(Otps otps) {
        otpsRepository.confirmVerify(otps);
    }

    @Override
    public void updateTheCodeAfterResend(OtpsRequestDTO otps, UUID userId) {
        otpsRepository.updateTheCodeAfterResend(otps,userId);
    }

    @Override
    public boolean verifyOtp(Long otpCode, UUID userId) throws Exception {
        Otps otp = otpsRepository.getOtpsByOtpCode(otpCode);
        if (otp == null) {
            throw new Exception("No active OTP found for this user.");
        }

        if (!Objects.equals(otp.getOtpsCode(), otpCode) || Timestamp.valueOf(otp.getExpirationDate()).before(new Timestamp(System.currentTimeMillis()))) {
            return false;
        }
        return true;
    }
    @Override
    public void confirmVerifyByUserId(UUID userId) {
        otpsRepository.confirmVerifyByUserId(userId);
    }

    @Override
    public void updateOTPToResetPassword(OtpsRequestDTO otpsRequestDTO) {
        otpsRepository.updateOTPToResetPassword(otpsRequestDTO);
    }
}

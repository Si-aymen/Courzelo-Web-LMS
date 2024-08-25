package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.Role;
import org.example.courzelo.repositories.CodeVerificationRepository;
import org.example.courzelo.services.ICodeVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
@Service
@AllArgsConstructor
public class CodeVerificationService implements ICodeVerificationService {
    private static final Logger log = LoggerFactory.getLogger(CodeVerificationService.class);
    private final CodeVerificationRepository codeVerificationRepository;
    @Override
    public String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public CodeVerification getCodeByCode(String code) {
        return codeVerificationRepository.findByCode(code);
    }

    @Override
    public CodeVerification verifyCode(String codeToVerify) {
        log.info("Verifying code: {}", codeToVerify);
        CodeVerification codeVerification = codeVerificationRepository.findByCode(codeToVerify);
        if(codeVerification != null && !isCodeExpired(codeVerification)){
            log.info("found with repository: {}", codeVerification);
            return codeVerification;
        }
        log.info("Code not found or expired");
        return null;
    }
    private boolean isCodeExpired(CodeVerification codeVerification){
        return codeVerification.getExpiryDate().isBefore(Instant.now());
    }

    @Override
    public CodeVerification saveCode(CodeType codeType, String email, String code, Instant expiryDate) {
        return codeVerificationRepository.save(new CodeVerification(codeType, code,email, expiryDate));
    }

    @Override
    public CodeVerification saveCode(CodeType codeType, String code, String email, Role role,String institutionID, Instant expiryDate) {
        return codeVerificationRepository.save(new CodeVerification(codeType, code,email, role,institutionID, expiryDate));
    }

    @Override
    public void deleteCode(String email,CodeType codeType) {
        codeVerificationRepository.deleteByEmailAndCodeType(email, codeType);
    }

    @Override
    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deleteExpiredCodes() {
        log.info("Deleting expired codes");
        codeVerificationRepository.deleteAllByExpiryDateBefore(Instant.now());
    }
}

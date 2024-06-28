package org.example.courzelo.serviceImpls;

import lombok.AllArgsConstructor;
import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.repositories.CodeVerificationRepository;
import org.example.courzelo.services.ICodeVerificationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
@Service
@AllArgsConstructor
public class CodeVerificationService implements ICodeVerificationService {
    private final CodeVerificationRepository codeVerificationRepository;
    @Override
    public String generateCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String verifyCode(String codeToVerify) {
        CodeVerification codeVerification = codeVerificationRepository.findByCode(codeToVerify);
        if(codeVerification != null && codeVerification.getCode().equals(codeToVerify)){
            return codeVerification.getEmail();
        }
        return null;
    }

    @Override
    public CodeVerification saveCode(CodeType codeType, String email, String code, Instant expiryDate) {
        return codeVerificationRepository.save(new CodeVerification(codeType, code,email, expiryDate));
    }

    @Override
    public void deleteCode(String email,CodeType codeType) {
        codeVerificationRepository.deleteByEmailAndCodeType(email, codeType);
    }

    @Override
    public void deleteExpiredCodes() {
        codeVerificationRepository.deleteAllByExpiryDateBefore(Instant.now());
    }
}

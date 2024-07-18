package org.example.courzelo.services;

import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;

import java.time.Instant;

public interface ICodeVerificationService {
    String generateCode();
    String verifyCode(String codeToVerify);
    CodeVerification saveCode(CodeType codeType, String email, String code, Instant expiryDate);
    void deleteCode(String email, CodeType codeType);
    void deleteExpiredCodes();
}

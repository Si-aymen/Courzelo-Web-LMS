package org.example.courzelo.services;

import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.Role;

import java.time.Instant;

public interface ICodeVerificationService {
    String generateCode();
    CodeVerification getCodeByCode(String code);
    CodeVerification verifyCode(String codeToVerify);
    CodeVerification saveCode(CodeType codeType, String email, String code, Instant expiryDate);
    CodeVerification saveCode(CodeType codeType,String code, String email, Role role, String institutionID, Instant expiryDate);
    void deleteCode(String email, CodeType codeType);
    void deleteExpiredCodes();
}

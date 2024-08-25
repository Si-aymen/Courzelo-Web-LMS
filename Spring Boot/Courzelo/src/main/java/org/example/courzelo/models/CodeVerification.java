package org.example.courzelo.models;

import lombok.Data;
import org.example.courzelo.models.institution.Institution;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class CodeVerification {
    @Id
    private String id;
    private CodeType codeType;
    private String code;
    private String email;
    private Role role;
    private String institutionID;
    private Instant expiryDate;

    public CodeVerification() {
    }

    public CodeVerification(CodeType codeType, String code, String email, Instant expiryDate) {
        this.codeType = codeType;
        this.code = code;
        this.email = email;
        this.expiryDate = expiryDate;
    }

    public CodeVerification(CodeType codeType, String code, String email, Role role,String institutionID, Instant expiryDate) {
        this.codeType = codeType;
        this.code = code;
        this.email = email;
        this.role = role;
        this.institutionID = institutionID;
        this.expiryDate = expiryDate;
    }
}


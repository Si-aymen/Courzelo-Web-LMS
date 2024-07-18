package org.example.courzelo.repositories;

import org.example.courzelo.models.CodeType;
import org.example.courzelo.models.CodeVerification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface CodeVerificationRepository extends MongoRepository<CodeVerification, String> {
    void deleteByEmailAndCodeType(String email, CodeType codeType);
    void deleteAllByExpiryDateBefore(Instant expiryDate);
    CodeVerification findByCode(String code);

}

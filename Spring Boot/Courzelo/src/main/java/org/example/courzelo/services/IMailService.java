package org.example.courzelo.services;

import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.User;

public interface IMailService {
    void sendConfirmationEmail(User user, CodeVerification codeVerification);
}

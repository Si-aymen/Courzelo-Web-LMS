package org.example.courzelo.services;

import org.example.courzelo.models.CodeVerification;
import org.example.courzelo.models.institution.Institution;
import org.example.courzelo.models.User;

public interface IMailService {
    void sendConfirmationEmail(User user, CodeVerification codeVerification);
    void sendPasswordResetEmail(User user, CodeVerification codeVerification);
    void sendInstituionInvitationEmail(User user, Institution instituion, CodeVerification codeVerification);

}

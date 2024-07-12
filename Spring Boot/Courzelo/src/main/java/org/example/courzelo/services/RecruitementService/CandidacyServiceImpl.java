package org.example.courzelo.services.RecruitementService;

import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RecruitementEntities.Candidacy;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class CandidacyServiceImpl implements ICandidacyService{
    @Override
    public List<Candidacy> getAllCandidacy() {
        return null;
    }

    @Override
    public Candidacy getCandidacyById(String id) {
        return null;
    }

    @Override
    public void addCandidacy(Candidacy candidacy, String jobOfferId) {

    }

    @Override
    public void deleteCandidacy(String id) {

    }

    @Override
    public void updateCandidacy(String id, Candidacy candidacy) {

    }
}

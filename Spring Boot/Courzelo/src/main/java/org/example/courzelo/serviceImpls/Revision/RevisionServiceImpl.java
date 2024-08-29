package org.example.courzelo.serviceImpls.Revision;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.RevisionEntities.revision.Revision;
import org.example.courzelo.repositories.RevisionRepo.RevisionRepository;
import org.example.courzelo.services.Revision.IRevisionService;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RevisionServiceImpl implements IRevisionService {
    private final RevisionRepository revisionRepository;

@Override
public List<Revision> getAllRevisions() {
    return revisionRepository.findAll();
}

    @Override
    public Revision createRevision(Revision revision) {
        return revisionRepository.save(revision);
    }

    @Override
    public Revision getRevisionById(String id) {
        Optional<Revision> revision = revisionRepository.findById(id);
        return revision.orElse(null);
    }

    @Override
    public Revision updateRevision(String id, Revision revision) {
        if (revisionRepository.existsById(id)) {
            revision.setId(id);
            return revisionRepository.save(revision);
        }
        return null;
    }

    @Override
    public boolean deleteRevision(String id) {
        if (revisionRepository.existsById(id)) {
            revisionRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
package org.example.courzelo.services.Revision;

import org.example.courzelo.models.RevisionEntities.revision.Revision;

import java.util.List;

public interface IRevisionService {
    List<Revision> getAllRevisions();
    Revision createRevision(Revision revision);
    Revision getRevisionById(String id);
    Revision updateRevision(String id, Revision revision);
    boolean deleteRevision(String id);
}

package org.example.courzelo.repositories;

import org.example.courzelo.models.institution.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GroupRepository extends MongoRepository<Group,String> {
    Page<Group> findByInstitutionID(String institutionID, PageRequest pageRequest);
}

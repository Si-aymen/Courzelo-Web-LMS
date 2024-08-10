package org.example.courzelo.repositories.Forum;

import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.Ticket;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubforumRepository extends MongoRepository<SubForum, String> {
    List<SubForum> findByUser(User user);

}

package org.example.courzelo.repositories.Forum;

import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUser(User user);
    List<Post> findBySubforum(SubForum subforum);
}

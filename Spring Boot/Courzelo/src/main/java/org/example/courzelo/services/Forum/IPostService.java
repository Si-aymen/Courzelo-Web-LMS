package org.example.courzelo.services.Forum;



import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.User;

import java.util.List;
import java.util.Optional;

public interface IPostService {
    List<Post> getPostByUser(User user);
    Post getPost(String id);
    List<Post> getAllPosts();
    List<Post> getPostsBySub(String id);

    void deletePost(String id);

    Post savePost(Post post);
    Post updatePost(Post ticket);
    Post updatePost1(String id,String name, String content, String description);
}

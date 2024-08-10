package org.example.courzelo.serviceImpls.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Forum.PostRepository;
import org.example.courzelo.repositories.Forum.SubforumRepository;
import org.example.courzelo.services.Forum.IPostService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements IPostService {
    private final PostRepository repository;
    private final SubforumRepository subrepository;
    @Override
    public List<Post> getPostByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public Post getPost(String id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public List<Post> getAllPosts() {
        return repository.findAll();
    }

    @Override
    public List<Post> getPostsBySub(String id) {
        SubForum sub = subrepository.findById(id).orElseThrow(()-> new RuntimeException("Sub for post not found"));
        return repository.findBySubforum(sub);
    }

    @Override
    public void deletePost(String id) {
        repository.deleteById(id);
    }

    @Override
    public Post savePost(Post post) {
        return repository.save(post);
    }

    @Override
    public Post updatePost(Post post) {
        return repository.save(post);
    }

    @Override
    public Post updatePost1(String id,String name, String content, String description) {
        Post post = repository.findById(id).orElseThrow(()->new RuntimeException("Post update not found")) ;
        post.setPostName(name);
        post.setContent(content);
        post.setDescription(description);
        return repository.save(post);
    }

    @Transactional
    public void upvotePost(String postId, String userId) {
        Post post = repository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        // Initialize the upvotedBy list if it is null
        if (post.getUpvotedBy() == null) {
            post.setUpvotedBy(new ArrayList<>());
        }

        // Initialize the downvotedBy list if it is null
        if (post.getDownvotedBy() == null) {
            post.setDownvotedBy(new ArrayList<>());
        }

        // Check if the user has already upvoted the comment
        if (post.getUpvotedBy().contains(userId)) {
            // Remove the upvote if the user has already upvoted
            post.getUpvotedBy().remove(userId);
        } else {
            // Add the user to the upvotedBy list
            post.getUpvotedBy().add(userId);

            // If the user had downvoted the comment, remove the downvote
            post.getDownvotedBy().remove(userId);
        }

        // Save the updated comment
        repository.save(post);
    }

    public boolean isPostUpvotedByUser(String commentId, String userId) {
        Optional<Post> postOpt = repository.findById(commentId);
        return postOpt.map(comment -> comment.getUpvotedBy().contains(userId)).orElse(false);
    }

    public int getUpvoteCount(String commentId) {
        Optional<Post> postOpt = repository.findById(commentId);
        return postOpt.map(comment -> comment.getUpvotedBy().size()).orElse(0);
    }
    @Transactional
    public void downvotePost(String postId, String userId) {
        Post post = repository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Initialize the upvotedBy list if it is null
        if (post.getUpvotedBy() == null) {
            post.setUpvotedBy(new ArrayList<>());
        }

        // Initialize the downvotedBy list if it is null
        if (post.getDownvotedBy() == null) {
            post.setDownvotedBy(new ArrayList<>());
        }

        // Check if the user has already upvoted the comment
        if (post.getDownvotedBy().contains(userId)) {
            // Remove the upvote if the user has already upvoted
            post.getDownvotedBy().remove(userId);
        } else {
            // Add the user to the upvotedBy list
            post.getDownvotedBy().add(userId);

            // If the user had downvoted the comment, remove the downvote
            post.getUpvotedBy().remove(userId);
        }

        // Save the updated comment
        repository.save(post);
    }

    public boolean isPostDownvotedByUser(String postId, String userId) {
        Optional<Post> postOpt = repository.findById(postId);
        return postOpt.map(comment -> comment.getDownvotedBy().contains(userId)).orElse(false);
    }
    public int getDownvoteCount(String postId) {
        Optional<Post> postOpt = repository.findById(postId);
        return postOpt.map(comment -> comment.getDownvotedBy().size()).orElse(0);
    }
}

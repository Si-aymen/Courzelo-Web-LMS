package org.example.courzelo.serviceImpls.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Forum.CommentRepository;
import org.example.courzelo.repositories.Forum.PostRepository;
import org.example.courzelo.services.Forum.ICommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;

    @Override
    public List<Comment> getCommentByPost(String postid) {
        Post post = postRepository.findById(postid).orElseThrow(()->new RuntimeException("Post Comment Not FOund"));
        return repository.findByPost(post);
    }
    @Override
    public Comment getCommentById(String id) {
        return repository.findById(id).get();
    }

    @Override
    public List<Comment> getCommentByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public void deleteComment(String id) {
        repository.deleteById(id);
    }

    @Override
    public Comment saveComment(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment) {
        return repository.save(comment);
    }

    @Override
    public Comment updateComment1(String id, String text) {
        Comment comment = repository.findById(id).orElseThrow(()->new RuntimeException("Update id"));
        comment.setText(text);
        return repository.save(comment);
    }

    @Transactional
    public void upvoteComment(String commentId, String userId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Initialize the upvotedBy list if it is null
        if (comment.getUpvotedBy() == null) {
            comment.setUpvotedBy(new ArrayList<>());
        }

        // Initialize the downvotedBy list if it is null
        if (comment.getDownvotedBy() == null) {
            comment.setDownvotedBy(new ArrayList<>());
        }

        // Check if the user has already upvoted the comment
        if (comment.getUpvotedBy().contains(userId)) {
            // Remove the upvote if the user has already upvoted
            comment.getUpvotedBy().remove(userId);
        } else {
            // Add the user to the upvotedBy list
            comment.getUpvotedBy().add(userId);

            // If the user had downvoted the comment, remove the downvote
            comment.getDownvotedBy().remove(userId);
        }

        // Save the updated comment
        repository.save(comment);
    }

    // Remove a user from the upvotedBy list
    @Transactional
    public void removeUpvoteFromComment(String commentId, String userId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        if (comment.getUpvotedBy() == null) {
            comment.setUpvotedBy(new ArrayList<>()); // Initialize if needed
        }
            if (comment.getUpvotedBy().contains(userId)) {
                comment.getUpvotedBy().remove(userId);
                repository.save(comment);
            }
         else {
            throw new CommentNotFoundException("Comment not found with id: " + commentId);
        }
    }

    // Check if a user has upvoted the comment
    public boolean isCommentUpvotedByUser(String commentId, String userId) {
        Optional<Comment> commentOpt = repository.findById(commentId);
        return commentOpt.map(comment -> comment.getUpvotedBy().contains(userId)).orElse(false);
    }

    // Get the number of upvotes for a comment
    public int getUpvoteCount(String commentId) {
        Optional<Comment> commentOpt = repository.findById(commentId);
        return commentOpt.map(comment -> comment.getUpvotedBy().size()).orElse(0);
    }



    @Transactional
    public void downvoteComment(String commentId, String userId) {
        Comment comment = repository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // Initialize the upvotedBy list if it is null
        if (comment.getUpvotedBy() == null) {
            comment.setUpvotedBy(new ArrayList<>());
        }

        // Initialize the downvotedBy list if it is null
        if (comment.getDownvotedBy() == null) {
            comment.setDownvotedBy(new ArrayList<>());
        }

        // Check if the user has already upvoted the comment
        if (comment.getDownvotedBy().contains(userId)) {
            // Remove the upvote if the user has already upvoted
            comment.getDownvotedBy().remove(userId);
        } else {
            // Add the user to the upvotedBy list
            comment.getDownvotedBy().add(userId);

            // If the user had downvoted the comment, remove the downvote
            comment.getUpvotedBy().remove(userId);
        }

        // Save the updated comment
        repository.save(comment);
    }

    // Remove a user from the upvotedBy list
    @Transactional
    public void removeDownvoteFromComment(String commentId, String userId) {
        Optional<Comment> commentOpt = repository.findById(commentId);
        if (commentOpt.isPresent()) {
            Comment comment = commentOpt.get();
            if (comment.getDownvotedBy().contains(userId)) {
                comment.getDownvotedBy().remove(userId);
                repository.save(comment);
            }
        } else {
            throw new CommentNotFoundException("Comment not found with id: " + commentId);
        }
    }

    // Check if a user has upvoted the comment
    public boolean isCommentDownvotedByUser(String commentId, String userId) {
        Optional<Comment> commentOpt = repository.findById(commentId);
        return commentOpt.map(comment -> comment.getDownvotedBy().contains(userId)).orElse(false);
    }
    public int getDownvoteCount(String commentId) {
        Optional<Comment> commentOpt = repository.findById(commentId);
        return commentOpt.map(comment -> comment.getDownvotedBy().size()).orElse(0);
    }


}

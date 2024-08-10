package org.example.courzelo.controllers.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.CommentREQ;
import org.example.courzelo.dto.requests.PostREQ;
import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Forum.CommentServiceImpl;
import org.example.courzelo.serviceImpls.Forum.PostServiceImpl;
import org.example.courzelo.serviceImpls.Forum.SubForumServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/v1/comment")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final PostServiceImpl postService;
    private final CommentServiceImpl commentService;
    private final UserRepository userRepository;

    @GetMapping("/post/{id}")
    public List<Comment> getComments(@PathVariable String id){
        return commentService.getCommentByPost(id);
    }

    @GetMapping("/get/{id}")
    public Comment getComment(@PathVariable String id){
        return commentService.getCommentById(id);
    }


    @PostMapping("/add")
    public ResponseEntity<Object> addComment(@RequestBody CommentREQ commentREQ) {
        try {
            Post post = postService.getPost(commentREQ.getPost());
            User user = userRepository.findUserByEmail(commentREQ.getUser());
            Comment comment = new Comment();
            comment.setText(commentREQ.getText());
            comment.setUser(user);
            comment.setPost(post);
            commentService.saveComment(comment);
            return ResponseEntity.ok().body("{\"message\": \"Comment ajouté avec succès!\"}"); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Échec de l'ajout du Comment: " + e.getMessage() + "\"}"); // Return JSON object
        }
    }

    @GetMapping("/user/{email}")
    public List<Comment> getPostByUser(@PathVariable String email) {
        User user = userRepository.findUserByEmail(email);
        return commentService.getCommentByUser(user);
    }


    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deletepost(@PathVariable String ID) {
        try {
            commentService.deleteComment(ID);
            return ResponseEntity.ok(Collections.singletonMap("message", "Sub deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Sub to delete ticket"));
        }
    }


    @PutMapping("/update1/{id}")
    public Comment updatepost(@PathVariable String id,@RequestBody CommentREQ comment) {
        String text = comment.getText();
        return commentService.updateComment1(id,text);
    }


    @PostMapping("/{commentId}/upvote")
    public ResponseEntity<Map<String, String>> upvoteComment(@PathVariable String commentId, @RequestParam String userId) {
        try {
            commentService.upvoteComment(commentId, userId);

            // Create a response map with the message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment downvoted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace for debugging
            // Create a response map with the error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/{commentId}/remove-upvote")
    public ResponseEntity<String> removeUpvoteFromComment(@PathVariable String commentId, @RequestParam String userId) {

        commentService.removeUpvoteFromComment(commentId, userId);
        return ResponseEntity.ok("Upvote removed successfully");
    }

    @GetMapping("/{commentId}/upvote-count")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable String commentId) {
        int upvoteCount = commentService.getUpvoteCount(commentId);
        return ResponseEntity.ok(upvoteCount);
    }

    @GetMapping("/{commentId}/is-upvoted")
    public ResponseEntity<Boolean> isCommentUpvotedByUser(@PathVariable String commentId, @RequestParam String userId) {
        boolean isUpvoted = commentService.isCommentUpvotedByUser(commentId, userId);
        return ResponseEntity.ok(isUpvoted);
    }

    @PostMapping("/{commentId}/downvote")
    public ResponseEntity<Map<String, String>> downvoteComment(@PathVariable String commentId, @RequestParam String userId) {
        try {
            commentService.downvoteComment(commentId, userId);

            // Create a response map with the message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Comment downvoted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace for debugging

            // Create a response map with the error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{commentId}/is-downvoted")
    public ResponseEntity<Boolean> isCommentDownvotedByUser(@PathVariable String commentId, @RequestParam String userId) {
        boolean isUpvoted = commentService.isCommentDownvotedByUser(commentId, userId);
        return ResponseEntity.ok(isUpvoted);
    }

    @GetMapping("/{commentId}/downvote-count")
    public ResponseEntity<Integer> getDownvoteCount(@PathVariable String commentId) {
        int upvoteCount = commentService.getDownvoteCount(commentId);
        return ResponseEntity.ok(upvoteCount);
    }
}

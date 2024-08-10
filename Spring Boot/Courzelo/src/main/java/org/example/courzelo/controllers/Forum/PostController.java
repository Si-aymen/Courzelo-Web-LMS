package org.example.courzelo.controllers.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.PostREQ;
import org.example.courzelo.dto.requests.SubForumREQ;
import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Forum.PostRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Forum.CommentServiceImpl;
import org.example.courzelo.serviceImpls.Forum.PostServiceImpl;
import org.example.courzelo.serviceImpls.Forum.SubForumServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/v1/post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostServiceImpl postService;
    private final PostRepository repository;
    private final SubForumServiceImpl subForumService;
    private final UserRepository userRepository;
    private  final CommentServiceImpl commentService;

    @GetMapping("/all/{id}")
    public List<Post> getPosts(@PathVariable String id){
        return postService.getPostsBySub(id);
    }

    @GetMapping("/get/{id}")
    public Post getPost(@PathVariable String id){
        return postService.getPost(id);
    }

    @GetMapping("/getall")
    public List<Post> getAll(){
        return postService.getAllPosts();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addPost(@RequestBody PostREQ postREQ) {
        try {
            // Log the incoming request
            System.out.println("Received PostREQ: " + postREQ);

            Post post = new Post();
            SubForum subForum = subForumService.getSubForum(postREQ.getSubforum());
            User user = userRepository.findUserByEmail(postREQ.getUser());

            // Check if subForum and user are not null
            if (subForum == null) {
                System.err.println("SubForum not found for ID: " + postREQ.getSubforum());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"SubForum not found\"}");
            }

            if (user == null) {
                System.err.println("User not found for email: " + postREQ.getUser());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"User not found\"}");
            }

            // Ensure the user's email is not null
            if (user.getEmail() == null) {
                System.err.println("User email is null for user: " + postREQ.getUser());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"error\": \"User email is null\"}");
            }

            // Set the properties of the post
            post.setPostName(postREQ.getPostName());
            post.setContent(postREQ.getContent());
            post.setDescription(postREQ.getDescription());
            post.setUser(user);
            post.setSubforum(subForum);

            // Save the post
            postService.savePost(post);

            System.out.println("Post saved successfully");
            return ResponseEntity.ok().body("{\"message\": \"Post added successfully!\"}");
        } catch (Exception e) {
            System.err.println("Error adding post: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Failed to add post: " + e.getMessage() + "\"}");
        }
    }


    @GetMapping("/user/{email}")
    public List<Post> getPostByUser(@PathVariable String email) {
        User user = userRepository.findUserByEmail(email);
        return postService.getPostByUser(user);
    }


    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deletePost(@PathVariable String ID) {
        try {
            // Check if post exists
            Optional<Post> postOptional = repository.findById(ID);
            if (!postOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Post not found"));
            }

            // Retrieve and delete comments
            List<Comment> comments = commentService.getCommentByPost(ID);
            for (Comment comment : comments) {
                commentService.deleteComment(comment.getId());
            }

            // Delete the post
            postService.deletePost(ID);

            // Return success response
            return ResponseEntity.ok(Collections.singletonMap("message", "Post deleted successfully"));

        } catch (Exception e) {
            // Log the error
            e.printStackTrace();

            // Return error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Failed to delete post"));
        }
    }



    @PutMapping("/update1/{id}")
    public Post updatepost(@PathVariable String id,@RequestBody PostREQ post) {
        String name = post.getPostName();
        String content = post.getContent();
        String description = post.getDescription();
        return postService.updatePost1(id, name, content, description);
    }

    @PostMapping("/{postId}/upvote")
    public ResponseEntity<Map<String, String>> upvotePost(@PathVariable String postId, @RequestParam String userId) {
        try {
            postService.upvotePost(postId, userId);

            // Create a response map with the message
            Map<String, String> response = new HashMap<>();
            response.put("message", "post upvoted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace for debugging
            // Create a response map with the error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{postId}/upvote-count")
    public ResponseEntity<Integer> getUpvoteCount(@PathVariable String postId) {
        int upvoteCount = postService.getUpvoteCount(postId);
        return ResponseEntity.ok(upvoteCount);
    }

    @GetMapping("/{postId}/is-upvoted")
    public ResponseEntity<Boolean> isPostUpvotedByUser(@PathVariable String postId, @RequestParam String userId) {
        boolean isUpvoted = postService.isPostUpvotedByUser(postId, userId);
        return ResponseEntity.ok(isUpvoted);
    }

    @PostMapping("/{postId}/downvote")
    public ResponseEntity<Map<String, String>> downvotePost(@PathVariable String postId, @RequestParam String userId) {
        try {
            postService.downvotePost(postId, userId);

            // Create a response map with the message
            Map<String, String> response = new HashMap<>();
            response.put("message", "Post downvoted successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace for debugging

            // Create a response map with the error message
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @GetMapping("/{postId}/is-downvoted")
    public ResponseEntity<Boolean> isPostDownvotedByUser(@PathVariable String postId, @RequestParam String userId) {
        boolean isUpvoted = postService.isPostDownvotedByUser(postId, userId);
        return ResponseEntity.ok(isUpvoted);
    }

    @GetMapping("/{postId}/downvote-count")
    public ResponseEntity<Integer> getDownvoteCount(@PathVariable String postId) {
        int upvoteCount = postService.getDownvoteCount(postId);
        return ResponseEntity.ok(upvoteCount);
    }

}

package org.example.courzelo.controllers.Forum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.courzelo.dto.requests.SubForumREQ;
import org.example.courzelo.models.Forum.Comment;
import org.example.courzelo.models.Forum.Post;
import org.example.courzelo.models.Forum.SubForum;
import org.example.courzelo.models.User;
import org.example.courzelo.repositories.Forum.SubforumRepository;
import org.example.courzelo.repositories.UserRepository;
import org.example.courzelo.serviceImpls.Forum.PostServiceImpl;
import org.example.courzelo.services.Forum.ISubForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequestMapping("/v1/subforum")
@RestController
@RequiredArgsConstructor
@Slf4j
public class SubForumController {
    private final ISubForumService subForumService;
    private final SubforumRepository repository;
    private final UserRepository userRepository;
    private final PostServiceImpl postService;

    @GetMapping("/all")
    public List<SubForum> getSub() {
        return subForumService.getSubForums();
    }

    @PostMapping("/add")
    public ResponseEntity<Object> addSub(@RequestBody SubForumREQ subForumREQ) {
        try {
            SubForum subForum = new SubForum();
            User user = userRepository.findUserByEmail(subForumREQ.getUser());
            subForum.setName(subForumREQ.getName());
            subForum.setDescription(subForumREQ.getDescription());
            subForum.setUser(user);
            subForumService.saveSub(subForum);
            return ResponseEntity.ok().body("{\"message\": \"Sub ajouté avec succès!\"}"); // Return JSON object
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\": \"Échec de l'ajout du sub: " + e.getMessage() + "\"}"); // Return JSON object
        }
    }

    @GetMapping("/user/{email}")
    public List<SubForum> getTSubForumByUser(@PathVariable String email) {
        User user = userRepository.findUserByEmail(email);
        return subForumService.getUserSubs(user);
    }
    @GetMapping("/get/{id}")
    public SubForum getSubs(@PathVariable String id) {
        return subForumService.getSubForum(id);
    }

    @DeleteMapping("/delete/{ID}")
    public ResponseEntity<?> deleteSub(@PathVariable String ID) {
        try {
            // Check if post exists
            Optional<SubForum> postOptional = repository.findById(ID);
            if (!postOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("message", "Sub not found"));
            }

            // Retrieve and delete comments
            List<Post> posts = postService.getPostsBySub(ID);
            for (Post post : posts) {
                postService.deletePost(post.getId());
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
    public SubForum updateSubs(@PathVariable String id,@RequestBody SubForumREQ sub) {
        String name = sub.getName();
        String description = sub.getDescription();
        return subForumService.updateSubForum1(id, name, description);
    }


}

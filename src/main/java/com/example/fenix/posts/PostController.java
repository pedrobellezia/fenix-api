package com.example.fenix.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;
import java.util.List;

import com.example.fenix.comments.CommentService;
import com.example.fenix.comments.Comment;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/posts", "/api/post"})
@CrossOrigin(origins = "*") 
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentService commentService;

    @PostMapping
    public org.springframework.http.ResponseEntity<?> createPost(@RequestBody Post post) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("DEBUG POST: Email from security context: " + email);
        User user = userRepository.findByEmail(email);
        System.out.println("DEBUG POST: User from repository: " + (user != null ? user.getId() : "null"));
        if (user == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }
        post.setUser(user);

        if (post.getMedia() != null) {
            for (com.example.fenix.postmedia.PostMedia m : post.getMedia()) {
                m.setPost(post);
            }
        }

        return org.springframework.http.ResponseEntity.ok(postService.criarPost(post));
    }

    @GetMapping
    public List<Post> getFeed() {
        return postService.listarFeed();
    }

    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> getPostById(@PathVariable UUID id) {
        Post post = postService.buscarPorId(id);
        if (post == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("Post não encontrado");
        }
        return org.springframework.http.ResponseEntity.ok(post);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByPost(@PathVariable UUID id) {
        return commentService.listarComentariosPorPost(id);
    }
    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> deletePost(@PathVariable UUID id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email);
        if (currentUser == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body("User not found for email: " + email);
        }

        Post post = postService.buscarPorId(id);
        if (post == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("Post not found");
        }

        if (!post.getUser().getId().equals(currentUser.getId()) && !"ADMIN".equals(currentUser.getRole())) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body("Unauthorized: You can only delete your own posts unless you are an ADMIN");
        }

        postService.deletarPost(id);
        return org.springframework.http.ResponseEntity.ok().build();
    }
}
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

    @Autowired
    private com.example.fenix.postmedia.PostMediaService postMediaService;

    @PostMapping(consumes = {"multipart/form-data"})
    public org.springframework.http.ResponseEntity<?> createPost(
            @RequestPart("post") Post post,
            @RequestPart(value = "files", required = false) List<org.springframework.web.multipart.MultipartFile> files) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("DEBUG POST: Email from security context: " + email);
        User user = userRepository.findByEmail(email);
        System.out.println("DEBUG POST: User from repository: " + (user != null ? user.getId() : "null"));
        if (user == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }
        post.setUser(user);

        if (!"ADMIN".equals(user.getRole())) {
            post.setHomepage(false);
        }

        Post savedPost = postService.criarPost(post);

        if (files != null && !files.isEmpty()) {
            for (org.springframework.web.multipart.MultipartFile file : files) {
                postMediaService.upload(savedPost.getId(), file);
            }
            savedPost = postService.buscarPorId(savedPost.getId());
        }

        return org.springframework.http.ResponseEntity.ok(savedPost);
    }

    @GetMapping
    public List<Post> getFeed(@RequestParam(defaultValue = "false", required = false) boolean homepage) {
        return postService.listarFeed(homepage);
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
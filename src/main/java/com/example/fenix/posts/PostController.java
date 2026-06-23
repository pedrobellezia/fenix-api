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
    public Post createPost(@RequestBody Post post) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("DEBUG POST: Email from security context: " + email);
        User user = userRepository.findByEmail(email);
        System.out.println("DEBUG POST: User from repository: " + (user != null ? user.getId() : "null"));
        if (user == null) {
            throw new RuntimeException("User not found for email: " + email);
        }
        post.setUser(user);

        // Se houver mídcrie ias enviadas junto com o post, vinculamos o post a cada uma delas
        if (post.getMedia() != null) {
            for (com.example.fenix.postmedia.PostMedia m : post.getMedia()) {
                m.setPost(post);
            }
        }

        return postService.criarPost(post);
    }

    @GetMapping
    public List<Post> getFeed() {
        return postService.listarFeed();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable UUID id) {
        return postService.buscarPorId(id);
    }

    @GetMapping("/{id}/comments")
    public List<Comment> getCommentsByPost(@PathVariable UUID id) {
        return commentService.listarComentariosPorPost(id);
    }
}
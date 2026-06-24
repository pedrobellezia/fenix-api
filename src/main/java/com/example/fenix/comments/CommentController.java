package com.example.fenix.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public org.springframework.http.ResponseEntity<?> criarComentario(@RequestBody Comment comment) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("User not found for email: " + email);
        }
        comment.setUser(user);
        return org.springframework.http.ResponseEntity.ok(commentService.adicionarComentario(comment));
    }

    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> deleteComment(@PathVariable UUID id) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            User currentUser = userRepository.findByEmail(email);
            if (currentUser == null) {
                return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body("User not found for email: " + email);
            }

            Comment comment = commentService.buscarPorId(id);
            if (comment == null) {
                return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("Comment not found");
            }

            if (!comment.getUser().getId().equals(currentUser.getId()) && !"ADMIN".equals(currentUser.getRole())) {
                return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body("Unauthorized: You can only delete your own comments unless you are an ADMIN");
            }

            commentService.deletarComentario(id);
            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage() + " | Causa: " + (e.getCause() != null ? e.getCause().getMessage() : "null"));
        }
    }
}
package com.example.fenix.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment adicionarComentario(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> listarComentariosPorPost(UUID postId) {
        return commentRepository.findByPostId(postId);
    }
}
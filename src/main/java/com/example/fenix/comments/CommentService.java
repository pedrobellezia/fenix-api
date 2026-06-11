package com.example.fenix.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment adicionarComentario(Comment comment) {
        return commentRepository.save(comment);
    }
}
package com.example.fenix.comments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public Comment criarComentario(@RequestBody Comment comment) {
        return commentService.adicionarComentario(comment);
    }
}
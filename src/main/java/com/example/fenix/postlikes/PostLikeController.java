package com.example.fenix.postlikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postlikes")
@CrossOrigin(origins = "*") 
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping
    public PostLike curtir(@RequestBody PostLike postLike) {
        return postLikeService.curtirPost(postLike);
    }
}
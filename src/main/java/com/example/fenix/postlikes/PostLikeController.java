package com.example.fenix.postlikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;

@RestController
@RequestMapping("/api/postlikes")
@CrossOrigin(origins = "*") 
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public PostLike curtir(@RequestBody PostLike postLike) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        postLike.setUser(user);
        return postLikeService.curtirPost(postLike);
    }
}
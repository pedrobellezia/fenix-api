package com.example.fenix.postlikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    public PostLike curtirPost(PostLike postLike) {
        // Futuramente, é aqui que vocês podem colocar uma regra para 
        // impedir que o mesmo usuário curta o mesmo post duas vezes!
        return postLikeRepository.save(postLike);
    }
}
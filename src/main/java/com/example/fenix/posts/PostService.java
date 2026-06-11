package com.example.fenix.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post criarPost(Post post) {
        // Aqui no futuro podemos colocar regras de negócio (ex: validar palavras impróprias)
        return postRepository.save(post);
    }

    public List<Post> listarFeed() {
        return postRepository.findAll();
    }
}
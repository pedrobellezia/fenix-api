package com.example.fenix.posts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post criarPost(Post post) {
        // Aqui no futuro podemos colocar regras de negócio (ex: validar palavras impróprias)
        return postRepository.save(post);
    }

    public List<Post> listarFeed(boolean homepage) {
        return postRepository.findByHomepageOrderByCreatedAtDesc(homepage);
    }

    public Post buscarPorId(UUID id) {
        return postRepository.findById(id).orElse(null);
    }
    public void deletarPost(UUID id) {
        postRepository.deleteById(id);
    }
}
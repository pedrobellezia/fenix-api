package com.example.fenix.postlikes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    public PostLike curtirPost(PostLike postLike) {
        
        //  o java pergunta pro banco: "Esse usuário já reagiu a este post?"
        Optional<PostLike> reacaoExistente = postLikeRepository.findByPostAndUser(postLike.getPost(), postLike.getUser());

        if (reacaoExistente.isPresent()) {
            PostLike reacaoAntiga = reacaoExistente.get();
            
            // se ele clicou na MESMA figurinha, a gente "tira o like" (deleta)
            if (reacaoAntiga.getReactionType() == postLike.getReactionType()) {
                postLikeRepository.delete(reacaoAntiga);
                return null; 
            } 
            // se ele escolheu uma figurinha DIFERENTE, a gente atualiza a escolha dele
            else {
                reacaoAntiga.setReactionType(postLike.getReactionType());
                return postLikeRepository.save(reacaoAntiga);
            }
        }

        // se não achou nenhuma reação anterior, é a primeira vez. salva normal!
        return postLikeRepository.save(postLike);
    }
}
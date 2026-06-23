package com.example.fenix.postlikes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import com.example.fenix.posts.Post;
import com.example.fenix.users.User;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    
    // Método mágico que procura se já existe uma reação deste usuário neste post
    Optional<PostLike> findByPostAndUser(Post post, User user);
    
}
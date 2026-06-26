package com.example.fenix.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findByHomepageOrderByCreatedAtDesc(boolean homepage);
}
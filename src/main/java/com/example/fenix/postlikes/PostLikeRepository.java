package com.example.fenix.postlikes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
}
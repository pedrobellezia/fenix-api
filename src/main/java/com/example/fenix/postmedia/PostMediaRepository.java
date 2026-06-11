package com.example.fenix.postmedia;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMediaRepository extends JpaRepository<PostMedia, UUID> {
}
package com.example.fenix.posts;

import com.example.fenix.users.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

   @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "media_type")
    private String mediaType; // Para fotos/vídeos futuramente

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // O Spring preenche a data sozinho!

    @Column(nullable = false)
    private boolean active = true; // Todo post já nasce ativo!

    // A Mágica do Relacionamento: Todo post pertence a 1 usuário
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
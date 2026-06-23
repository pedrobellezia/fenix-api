package com.example.fenix.users;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    // username único para exibição, pode ser o mesmo que o name ou algo diferente, mas deve ser único
    @Column(unique = true)
    private String displayName;

    @Column(length = 300, nullable = true)
    private String bio;

    @Email(message = "Email deve ser válido")
    @Column(length = 150, unique = true, nullable = false)
    private String email;

    @Column(name = "pic_url", length = 500)
    private String picUrl = "https://www.gravatar.com/avatar/00000000000000000000000000000000?d=mp&f=y";

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "treatment_phase", length = 50, nullable = true)
    private String treatmentPhase;

    @org.hibernate.annotations.CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @org.hibernate.annotations.UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

   
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<com.example.fenix.posts.Post> posts = new java.util.ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<com.example.fenix.comments.Comment> comments = new java.util.ArrayList<>();




    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getTreatmentPhase() {
        return treatmentPhase;
    }

    public void setTreatmentPhase(String treatmentPhase) {
        this.treatmentPhase = treatmentPhase;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public java.util.List<com.example.fenix.posts.Post> getPosts() {
        return posts;
    }
    public void setPosts(java.util.List<com.example.fenix.posts.Post> posts) {
        this.posts = posts;
    }

    public java.util.List<com.example.fenix.comments.Comment> getComments() {
        return comments;
    }
    public void setComments(java.util.List<com.example.fenix.comments.Comment> comments) {
        this.comments = comments;
    }
      

}
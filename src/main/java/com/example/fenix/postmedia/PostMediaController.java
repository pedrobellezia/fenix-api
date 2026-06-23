package com.example.fenix.postmedia;

import java.util.UUID;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/post-media")
public class PostMediaController {

    private final PostMediaService postMediaService;

    public PostMediaController(PostMediaService postMediaService) {
        this.postMediaService = postMediaService;
    }

    @GetMapping
    public List<PostMedia> list() {
        return postMediaService.findAll();
    }

    @GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> getById(@PathVariable UUID id) {
        PostMedia media = postMediaService.findById(id);
        if (media == null) {
            return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND).body("PostMedia not found");
        }
        return org.springframework.http.ResponseEntity.ok(media);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostMedia create(@RequestBody PostMedia postMedia) {
        return postMediaService.create(postMedia);
    }

    @PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> update(@PathVariable UUID id, @RequestBody PostMedia postMedia) {
        PostMedia updated = postMediaService.update(id, postMedia);
        if (updated == null) {
            return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND).body("PostMedia not found");
        }
        return org.springframework.http.ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> delete(@PathVariable UUID id) {
        boolean deleted = postMediaService.delete(id);
        if (!deleted) {
            return org.springframework.http.ResponseEntity.status(HttpStatus.NOT_FOUND).body("PostMedia not found");
        }
        return org.springframework.http.ResponseEntity.noContent().build();
    }
}
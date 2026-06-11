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
    public PostMedia getById(@PathVariable UUID id) {
        return postMediaService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostMedia create(@RequestBody PostMedia postMedia) {
        return postMediaService.create(postMedia);
    }

    @PutMapping("/{id}")
    public PostMedia update(@PathVariable UUID id, @RequestBody PostMedia postMedia) {
        return postMediaService.update(id, postMedia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        postMediaService.delete(id);
    }
}
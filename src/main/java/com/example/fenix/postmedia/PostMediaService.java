package com.example.fenix.postmedia;

import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.fenix.posts.Post;

import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class PostMediaService {

    private final PostMediaRepository postMediaRepository;

    public PostMediaService(PostMediaRepository postMediaRepository) {
        this.postMediaRepository = postMediaRepository;
    }

    public List<PostMedia> findAll() {
        return postMediaRepository.findAll();
    }

    public PostMedia findById(UUID id) {
        return postMediaRepository.findById(id).orElse(null);
    }

    public PostMedia create(PostMedia postMedia) {
        postMedia.setId(null);
        return postMediaRepository.save(postMedia);
    }

    public PostMedia update(UUID id, PostMedia postMedia) {
        PostMedia existingPostMedia = findById(id);
        if (existingPostMedia == null) return null;
        
        if (postMedia.getMediaType() != null) {
            existingPostMedia.setMediaType(postMedia.getMediaType());
        }

        if (postMedia.getMediaUrl() != null) {
            existingPostMedia.setMediaUrl(postMedia.getMediaUrl());        
        }

        if (postMedia.getMimeType() != null) {
            existingPostMedia.setMimeType(postMedia.getMimeType());
        }

        return postMediaRepository.save(existingPostMedia);
    }

    public boolean delete(UUID id) {
        if (!postMediaRepository.existsById(id)) {
            return false;
        }
        postMediaRepository.deleteById(id);
        return true;
    }
}
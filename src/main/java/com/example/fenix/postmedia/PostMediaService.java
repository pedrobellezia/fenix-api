package com.example.fenix.postmedia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.fenix.posts.Post;
import com.example.fenix.posts.PostRepository;

@Service
public class PostMediaService {

    private final PostMediaRepository postMediaRepository;
    private final PostRepository postRepository; // Precisamos do Post para amarrar a imagem a ele
    private final String UPLOAD_DIR = "uploads/"; // A pasta onde as imagens vão morar

    public PostMediaService(PostMediaRepository postMediaRepository, PostRepository postRepository) {
        this.postMediaRepository = postMediaRepository;
        this.postRepository = postRepository;
    }

    public List<PostMedia> findAll() {
        return postMediaRepository.findAll();
    }

    public PostMedia findById(UUID id) {
        return postMediaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PostMedia not found"));
    }

    public PostMedia upload(UUID postId, MultipartFile file) {
        try {
            // acha o post dono da imagem
            Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post não encontrado"));

            // cria a pasta 'uploads' no servidor se ela não existir
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // gera um nome único para o arquivo (para o paciente não sobrescrever a foto do outro)
            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + extension;

            // salva o arquivo fisicamente no computador
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // salva a URL no Banco de Dados
            PostMedia media = new PostMedia();
            media.setPost(post);
            media.setMimeType(file.getContentType());
            
            if (file.getContentType() != null && file.getContentType().startsWith("video/")) {
                media.setMediaType(PostMedia.MediaType.video);
            } else {
                media.setMediaType(PostMedia.MediaType.image);
            }
            
            media.setFilename(fileName); 

            return postMediaRepository.save(media);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a imagem", e);
        }
    }

    public PostMedia update(UUID id, PostMedia postMedia) {
        PostMedia existingPostMedia = findById(id);
        
        if (postMedia.getMediaType() != null) existingPostMedia.setMediaType(postMedia.getMediaType());
        if (postMedia.getFilename() != null) existingPostMedia.setFilename(postMedia.getFilename());        
        if (postMedia.getMimeType() != null) existingPostMedia.setMimeType(postMedia.getMimeType());

        return postMediaRepository.save(existingPostMedia);
    }

    public void delete(UUID id) {
        if (!postMediaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "PostMedia not found");
        }
        postMediaRepository.deleteById(id);
    }
}
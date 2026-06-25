package com.example.fenix.users;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final String UPLOAD_DIR = "uploads/";

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    public org.springframework.http.ResponseEntity<?> create(User user) {
        user.setId(null);

        if (user.getActive() == null) {
            user.setActive(true);
        }

        if (user.getTreatmentPhase() == null || user.getTreatmentPhase().trim().isEmpty()) {
            return org.springframework.http.ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Treatment phase is required");
        }

        User saved = userRepository.save(user);
        return org.springframework.http.ResponseEntity.ok(saved);
    }

    public boolean delete(UUID id) {
        if (!userRepository.existsById(id)) {
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    public User update(UUID id, User user) {
        User existingUser = findById(id);
        if (existingUser == null) return null;
        if (user.getName() != null) {
            existingUser.setName(user.getName());
        }
        if (user.getBio() != null) {
            existingUser.setBio(user.getBio());
        }
        if (user.getPicUrl() != null) {
            existingUser.setPicUrl(user.getPicUrl());
        }
        if (user.getActive() != null) {
            existingUser.setActive(user.getActive());
        }
        if (user.getTreatmentPhase() != null) {
            existingUser.setTreatmentPhase(user.getTreatmentPhase());
        }

        if (user.getEmail() != null) {
            existingUser.setEmail(user.getEmail());
        }

        return userRepository.save(existingUser);
    }

    public User uploadPhoto(UUID id, MultipartFile file) {
        try {
            User user = findById(id);
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalName = file.getOriginalFilename();
            String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf(".")) : "";
            String fileName = UUID.randomUUID().toString().replace("-", "").substring(0, 12) + extension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            user.setPicUrl(fileName);

            return userRepository.save(user);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar a imagem de perfil", e);
        }
    }
}
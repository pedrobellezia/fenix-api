package com.example.fenix.users;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

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
}
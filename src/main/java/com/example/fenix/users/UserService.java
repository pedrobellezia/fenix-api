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
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User create(User user) {
        user.setId(null);

        if (user.getActive() == null) {
            user.setActive(true);
        }

        if (user.getTreatmentPhase() == null || user.getTreatmentPhase().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treatment phase is required");
        }

        User saved = userRepository.save(user);
        return saved;
    }

    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    public User update(UUID id, User user) {
        User existingUser = findById(id);
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
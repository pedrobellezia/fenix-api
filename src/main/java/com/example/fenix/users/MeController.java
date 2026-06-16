package com.example.fenix.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/me")
public class MeController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Função interna para descobrir quem é o dono do Token que veio na requisição
    private User getUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email);
    }

    // Retorna os dados do perfil (GET /me)
    @GetMapping
    public ResponseEntity<?> getMe() {
        return ResponseEntity.ok(getUsuarioLogado());
    }

    // Atualiza o perfil dinamicamente (PATCH /me)
    @PatchMapping
    public ResponseEntity<String> updateMe(@RequestBody Map<String, Object> updates) {
        // Exigência do Pedro: Retornar erro se o body for vazio
        if (updates.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum campo foi informado para atualização.");
        }

        User user = getUsuarioLogado();

        // Atualiza apenas os campos que vieram no JSON do Insomnia
        if (updates.containsKey("name")) user.setName((String) updates.get("name"));
        if (updates.containsKey("bio")) user.setBio((String) updates.get("bio"));
        if (updates.containsKey("picUrl")) user.setPicUrl((String) updates.get("picUrl"));
        if (updates.containsKey("treatmentPhase")) user.setTreatmentPhase((String) updates.get("treatmentPhase"));
        if (updates.containsKey("displayName")) user.setDisplayName((String) updates.get("displayName"));
        
        if (updates.containsKey("password")) {
            user.setPassword(passwordEncoder.encode((String) updates.get("password"))); // Criptografa a nova senha
        }

        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    // Usar DELETE para inativar a conta
    @DeleteMapping
    public ResponseEntity<Void> disableMe() {
        User user = getUsuarioLogado();
        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}
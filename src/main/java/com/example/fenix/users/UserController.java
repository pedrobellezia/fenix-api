package com.example.fenix.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Essencial para o Vue.js conseguir acessar a API depois
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Rota para Cadastrar novo usuário (Paciente ou Profissional)
    @PostMapping
    public User criarUsuario(@RequestBody User user) {
        return userRepository.save(user);
    }

    // Rota para Listar todos os usuários salvos
    @GetMapping
    public List<User> listarTodos() {
        return userRepository.findAll();
    }
    @DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<?> deleteUser(@PathVariable java.util.UUID id) {
        String email = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(email);
        if (currentUser == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).body("User not found for email: " + email);
        }

        User targetUser = userRepository.findById(id).orElse(null);
        if (targetUser == null) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body("Target user not found");
        }

        if (!targetUser.getId().equals(currentUser.getId()) && !"ADMIN".equals(currentUser.getRole())) {
            return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.FORBIDDEN).body("Unauthorized: You can only delete your own account unless you are an ADMIN");
        }

        userRepository.deleteById(id);
        return org.springframework.http.ResponseEntity.ok().build();
    }
}
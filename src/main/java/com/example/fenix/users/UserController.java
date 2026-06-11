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
}
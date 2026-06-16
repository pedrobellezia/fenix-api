package com.example.fenix.security;

import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // DTO super rápido para o Login
    public record LoginRequest(String email, String password) {}

    // DTO para o Cadastro
    public record RegisterRequest(
            String name,
            String displayName,
            String email,
            String password,
            String role
    ) {}

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // O porteiro confere a senha
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        // gera o Token com os dados do usuário autenticado
        String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(loginRequest.email()));
        
        // Devolve o token puro para o Insomnia/Vue.js
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setDisplayName(request.displayName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setActive(true); // Conta nasce ativa
        
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }
}

package com.example.fenix.security;

import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@admin";
            User admin = userRepository.findByEmail(adminEmail);
            if (admin == null) {
                admin = new User();
                admin.setEmail(adminEmail);
                admin.setName("Administrator");
                admin.setDisplayName("admin");
                admin.setRole("ADMIN");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setBio("System Administrator");
                userRepository.save(admin);
                System.out.println("Admin user created successfully.");
            } else {
                boolean updated = false;
                if (!"ADMIN".equals(admin.getRole())) {
                    admin.setRole("ADMIN");
                    updated = true;
                }
                if (!passwordEncoder.matches("admin", admin.getPassword())) {
                    admin.setPassword(passwordEncoder.encode("admin"));
                    updated = true;
                }
                if (updated) {
                    userRepository.save(admin);
                    System.out.println("Admin user updated to ensure correct role and password.");
                }
            }
        };
    }
}

package com.example.fenix.security;

import com.example.fenix.users.User;
import com.example.fenix.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca o usuário no nosso banco usando o e-mail
        User user = userRepository.findByEmail(email);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }

        // Converte o nosso usuário Fênix para o usuário oficial de segurança do Spring
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // O "username" da segurança será o nosso e-mail
                .password(user.getPassword())
                .roles(user.getRole()) // Ex: "PACIENTE", "MEDICO"
                .build();
    }
}
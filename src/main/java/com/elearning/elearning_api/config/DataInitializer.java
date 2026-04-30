package com.elearning.elearning_api.config;

import com.elearning.elearning_api.entity.Utilisateur;
import com.elearning.elearning_api.enums.Role;
import com.elearning.elearning_api.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UtilisateurRepository utilisateurRepository,
                           PasswordEncoder passwordEncoder,
                           JdbcTemplate jdbcTemplate) {

        return args -> {
            

            if (utilisateurRepository.findByEmail("admin@gmail.com").isEmpty()) {

                Utilisateur admin = new Utilisateur();
                admin.setNom("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setMotDePasse(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setStatus("ACTIVE");

                utilisateurRepository.save(admin);

                System.out.println("✅ Admin créé !");
            }
        };
    }
}

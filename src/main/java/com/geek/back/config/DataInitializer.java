package com.geek.back.config;

import com.geek.back.entities.Role;
import com.geek.back.entities.User;
import com.geek.back.repositories.RoleRepository;
import com.geek.back.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Crear roles si no existen
        List<String> rolesToCreate = Arrays.asList("ROLE_USER", "ROLE_ADMIN");
        for (String roleName : rolesToCreate) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                log.info("Creando rol por defecto: {}", roleName);
                return roleRepository.save(role);
            });
        }

        // Crear usuario admin si no existe
        String adminEmail = "admin@geekexpress.com";
        userRepository.findByEmail(adminEmail).ifPresentOrElse(
                user -> log.info("Usuario ADMIN ya existe: {}", adminEmail),
                () -> {
                    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                            .orElseThrow(() -> new RuntimeException("ROLE_ADMIN no existe"));

                    User admin = new User();
                    admin.setUserName("admin");
                    admin.setEmail("admin@geekexpress.com");
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRole(adminRole);
                    admin.setName("admin");
                    admin.setLastName("admin");// 👈 aquí va directo, no un Set
                    userRepository.save(admin);

                    userRepository.save(admin);
                    log.info("Usuario ADMIN creado con email: {}", adminEmail);
                }
        );
    }
}

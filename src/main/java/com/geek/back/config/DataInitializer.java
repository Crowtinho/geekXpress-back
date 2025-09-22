package com.geek.back.config;

import com.geek.back.entities.Role;
import com.geek.back.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<String> rolesToCreate = Arrays.asList("ROLE_USER", "ROLE_ADMIN");

        for (String roleName : rolesToCreate) {
            // Solo crear el rol si no existe
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role();
                role.setName(roleName);
                System.out.println("Creando rol: " + roleName);
                return roleRepository.save(role);
            });
        }
    }
}

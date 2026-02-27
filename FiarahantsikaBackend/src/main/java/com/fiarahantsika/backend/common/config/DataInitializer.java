package com.fiarahantsika.backend.common.config;

import com.fiarahantsika.backend.users.entities.Role;
import com.fiarahantsika.backend.users.entities.User;
import com.fiarahantsika.backend.users.repositories.RoleRepository;
import com.fiarahantsika.backend.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepo, UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role userRole = roleRepo.findByName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_USER")));

        Role adminRole = roleRepo.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));

        if (userRepo.findByUsername("Luc").isEmpty()) {
            User luc = new User();
            luc.setUsername("Luc");
            luc.setEmail("luc@fiarahantsika.com");
            luc.setPassword(passwordEncoder.encode("LusFiarahantsika!"));
            luc.setRoles(Collections.singleton(adminRole));

            userRepo.save(luc);
            System.out.println(">>> Utilisateur 'Luc' créé avec succès !");
        }
    }
}
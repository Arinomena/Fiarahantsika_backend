package com.fiarahantsika.backend.common.config;

import com.fiarahantsika.backend.users.entities.Role;
import com.fiarahantsika.backend.users.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepo;

    public DataInitializer(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepo.findByName("ROLE_USER").isEmpty()) {
            roleRepo.save(new Role("ROLE_USER"));
        }
        if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepo.save(new Role("ROLE_ADMIN"));
        }
    }
}

package com.fiarahantsika.backend.users.services;

import com.fiarahantsika.backend.users.entities.*;
import com.fiarahantsika.backend.users.dto.*;
import com.fiarahantsika.backend.users.mappers.UserMapper;
import com.fiarahantsika.backend.users.repositories.*;
import com.fiarahantsika.backend.common.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository u, RoleRepository r, PasswordEncoder e, JwtUtil j, UserMapper m) {
        this.userRepo = u; this.roleRepo = r;
        this.encoder = e; this.jwt = j; this.mapper = m;
    }

    @Override
    public UserDTO register(RegisterRequest r) {
        if (userRepo.findByUsername(r.username()).isPresent())
            throw new IllegalArgumentException("Username exists");
        var user = new User();
        user.setUsername(r.username());
        user.setEmail(r.email());
        user.setPassword(encoder.encode(r.password()));
        var role = roleRepo.findByName("ROLE_USER").orElseThrow();
        user.getRoles().add(role);
        userRepo.save(user);
        return mapper.toDTO(user);
    }

    @Override
    public AuthResponse login(LoginRequest r) {
        var user = userRepo.findByUsername(r.username())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!encoder.matches(r.password(), user.getPassword()))
            throw new IllegalArgumentException("Bad credentials");
        var token = jwt.generateToken(user);
        var roles = user.getRoles().stream().map(Role::getName).toList();
        return new AuthResponse(token, user.getUsername(), roles);
    }

    @Override
    public void initiatePasswordReset(PasswordResetRequest r) {
        var user = userRepo.findByEmail(r.email()).orElseThrow();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(Instant.now().plusSeconds(3600));
        userRepo.save(user);
        // TODO: envoyer email avec token
    }

    @Override
    public void confirmPasswordReset(PasswordResetConfirm r) {
        var user = userRepo.findByResetToken(r.token()).orElseThrow();
        if (user.getResetTokenExpiry().isBefore(Instant.now()))
            throw new IllegalArgumentException("Token expired");
        user.setPassword(encoder.encode(r.newPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepo.save(user);
    }
}

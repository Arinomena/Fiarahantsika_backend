package com.fiarahantsika.backend.users.controllers;

import com.fiarahantsika.backend.users.dto.UpdateUserRequest;
import com.fiarahantsika.backend.users.dto.UserDTO;
import com.fiarahantsika.backend.users.services.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final IUserService svc;

    public UserController(IUserService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> users = svc.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        UserDTO user = svc.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest dto) {
        return ResponseEntity.ok(svc.updateUser(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

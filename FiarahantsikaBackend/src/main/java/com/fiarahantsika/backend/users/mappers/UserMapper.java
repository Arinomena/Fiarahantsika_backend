package com.fiarahantsika.backend.users.mappers;

import com.fiarahantsika.backend.users.entities.User;
import com.fiarahantsika.backend.users.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserDTO toDTO(User u) {
        var roles = u.getRoles().stream().map(r -> r.getName()).toList();
        return new UserDTO(u.getId(), u.getUsername(), u.getEmail(), roles);
    }
}

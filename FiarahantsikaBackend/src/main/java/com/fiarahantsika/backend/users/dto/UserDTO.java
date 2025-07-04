package com.fiarahantsika.backend.users.dto;

import java.util.List;

public record UserDTO(Long id, String username, String email, List<String> roles) {

}

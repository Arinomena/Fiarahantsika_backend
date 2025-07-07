package com.fiarahantsika.backend.users.dto;

import java.util.List;

public record UpdateUserRequest(String email, List<String> roles)
{

}

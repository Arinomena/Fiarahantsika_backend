package com.fiarahantsika.backend.users.services;

import com.fiarahantsika.backend.users.dto.*;

public interface IUserService {
    UserDTO register(RegisterRequest req);
    AuthResponse login(LoginRequest req);
    void initiatePasswordReset(PasswordResetRequest req);
    void confirmPasswordReset(PasswordResetConfirm req);
}

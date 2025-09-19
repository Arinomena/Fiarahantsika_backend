package com.fiarahantsika.backend.users.services;

import com.fiarahantsika.backend.users.dto.*;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    UserDTO register(RegisterRequest r);
    AuthResponse login(LoginRequest r);
    void initiatePasswordReset(PasswordResetRequest r);
    void confirmPasswordReset(PasswordResetConfirm c);

    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO         updateUser(Long id, UpdateUserRequest dto);
    void            deleteUser(Long id);
    Page<UserDTO> getUsersPage(Pageable pageable);
}

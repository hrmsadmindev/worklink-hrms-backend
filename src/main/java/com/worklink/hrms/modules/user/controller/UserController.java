package com.worklink.hrms.modules.user.controller;

import com.worklink.hrms.common.dto.ApiResponse;
import com.worklink.hrms.modules.user.dto.UserDTO;
import com.worklink.hrms.modules.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('HR')")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getAllUsers();
            return ResponseEntity.ok(new ApiResponse<>("Users retrieved successfully", users, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve users: " + e.getMessage(), null, false));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        try {
            UserDTO user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse<>("User retrieved successfully", user, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to retrieve user: " + e.getMessage(), null, false));
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO created = userService.createUser(userDTO);
            return ResponseEntity.ok(new ApiResponse<>("User created successfully", created, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to create user: " + e.getMessage(), null, false));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, 
                                                         @Valid @RequestBody UserDTO userDTO) {
        try {
            UserDTO updated = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(new ApiResponse<>("User updated successfully", updated, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to update user: " + e.getMessage(), null, false));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(new ApiResponse<>("User deleted successfully", null, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Failed to delete user: " + e.getMessage(), null, false));
        }
    }
}
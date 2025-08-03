package com.worklink.hrms.modules.auth.controller;

import com.worklink.hrms.modules.auth.dto.AuthResponse;
import com.worklink.hrms.modules.auth.dto.LoginRequest;
import com.worklink.hrms.modules.auth.service.AuthService;
import com.worklink.hrms.common.dto.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.authenticate(loginRequest);
            return ResponseEntity.ok(new ApiResponse<>("Login successful", authResponse, true));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponse<>("Login failed: " + e.getMessage(), null, false));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(new ApiResponse<>("Logout successful", null, true));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken() {
        return ResponseEntity.ok(new ApiResponse<>("Token is valid", null, true));
    }
}
package com.finsure.controller;

import com.finsure.dto.CreateUserRequest;
import com.finsure.dto.ForgotPasswordRequest;
import com.finsure.dto.LoginRequest;
import com.finsure.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(
                authService.forgotPassword(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestHeader(value = "X-User-Role",
                           required = false) String callerRole,
            @RequestBody CreateUserRequest request) {


        System.out.println("=== /auth/register called ===");
        System.out.println("X-User-Role header received: " + callerRole);
        System.out.println("Request body name: " + request.getName());

        if (!"ADMIN".equals(callerRole)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can create users. Role received: " + callerRole);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.createUser(request));
    }
}

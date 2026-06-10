package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finsure.client.MemberClient;
import com.finsure.dto.CreateUserRequest;
import com.finsure.dto.ForgotPasswordRequest;
import com.finsure.dto.LoginRequest;
import com.finsure.dto.LoginResponse;
import com.finsure.entity.User;
import com.finsure.repository.UserRepository;
import com.finsure.security.JwtUtil;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuditServiceImpl auditService;
    
    @Autowired
    private MemberClient memberClient;

    private static final List<String> ALLOWED_ROLES = List.of(
    	    "TELLER", "LOAN_OFFICER", "BRANCH_MANAGER", "AUDITOR", "MEMBER"
    	);


    public String createUser(CreateUserRequest request) {

        if (request.getName() == null
                || request.getName().isBlank()) {
            throw new RuntimeException("Name is required");
        }
        if (request.getEmail() == null
                || request.getEmail().isBlank()) {
            throw new RuntimeException("Email is required");
        }
        if (request.getPhone() == null
                || request.getPhone().length() != 10) {
            throw new RuntimeException(
                    "Phone must be exactly 10 digits");
        }
        if (!ALLOWED_ROLES.contains(request.getRole())) {
            throw new RuntimeException(
                    "Invalid role. Allowed: " + ALLOWED_ROLES);
        }
        if (userRepository.existsByEmail(
                request.getEmail().toLowerCase())) {
            throw new RuntimeException(
                    "Email already registered: "
                    + request.getEmail());
        }



        String namePart = request.getName().length() >= 3
                ? request.getName().substring(0, 3)
                : request.getName();
        String phonePart = request.getPhone().length() >= 3
                ? request.getPhone().substring(
                        request.getPhone().length() - 3)
                : request.getPhone();
        String autoPassword = namePart + phonePart;

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(autoPassword));
        user.setRole(request.getRole());
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);


        try {
            auditService.log(
                    saved.getUserId(),
                    "USER_CREATED",
                    "USER",
                    saved.getUserId().toString(),
                    "Created by ADMIN. Role: " + saved.getRole());
        } catch (Exception e) {
            System.out.println("Audit log failed: "
                    + e.getMessage());
        }

        return "User created successfully. "
                + "Auto-generated password: " + autoPassword
                + " — Share this with the user.";
    }

    

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository
                .findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!user.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("User account is inactive");
        }

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }


        Long memberId = null;
        if ("MEMBER".equals(user.getRole())) {
            try {
                memberId = memberClient.getMemberIdByName(user.getName());
                System.out.println("=== MEMBER LOGIN ===");
                System.out.println("User name: " + user.getName());
                System.out.println("Member ID found: " + memberId);
            } catch (Exception e) {
                System.out.println("=== MEMBER LOOKUP FAILED ===");
                System.out.println("Error: " + e.getMessage());
            }
        }

        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole(), memberId);


        try {
            auditService.log(user.getUserId(), "LOGIN",
                    "USER", user.getUserId().toString(),
                    "Login from role: " + user.getRole());
        } catch (Exception e) {
            System.out.println("Audit log failed: " + e.getMessage());
        }

        return new LoginResponse(token, user.getRole(),
                "Login successful");
    }
  
    @Override
    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Account is inactive");
        }

        if (request.getNewPassword().length() < 5) {
            throw new RuntimeException("New password must be at least 5 characters");
        }


        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be same as current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return "Password updated successfully";
    }
}



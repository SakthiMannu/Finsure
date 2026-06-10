package com.finsure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finsure.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can view all users");
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN") && !role.equals("BRANCH_MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(
            @RequestHeader("X-User-Role") String userRole,
            @PathVariable String role) {

        if (!userRole.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can filter users by role");
        }
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }


    @PutMapping("/{id}/role")
    public ResponseEntity<?> updateRole(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestParam String newRole) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can update roles");
        }
        return ResponseEntity.ok(userService.updateUserRole(id, newRole));
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestParam String status) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can update user status");
        }
        return ResponseEntity.ok(userService.updateUserStatus(id, status));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete users");
        }
        return ResponseEntity.ok(userService.deleteUser(id));
    }
}

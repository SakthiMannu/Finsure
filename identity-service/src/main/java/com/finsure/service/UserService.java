package com.finsure.service;

import java.util.List;

import com.finsure.entity.User;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    String updateUserRole(Long id, String role);
    String updateUserStatus(Long id, String status);
    String deleteUser(Long id);
    List<User> getUsersByRole(String role);
}


package com.finsure.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finsure.entity.User;
import com.finsure.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    private static final List<String> VALID_ROLES = List.of(
            "ADMIN", "TELLER", "MEMBER", "LOAN_OFFICER", "BRANCH_MANAGER", "AUDITOR"
    );

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public String updateUserRole(Long id, String role) {

        User user = getUserById(id);

        if (!user.getStatus().equals("ACTIVE")) {
            throw new RuntimeException("Cannot update role of inactive user");
        }

        if (!VALID_ROLES.contains(role)) {
            throw new RuntimeException("Invalid role. Valid roles: " + VALID_ROLES);
        }

        user.setRole(role);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);


        auditService.log(id, "UPDATE_ROLE", "USER", id.toString(), "Role changed to " + role);

        return "User role updated to " + role;
    }

    @Override
    public String updateUserStatus(Long id, String status) {

        User user = getUserById(id);

        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new RuntimeException("Invalid status. Valid values: ACTIVE, INACTIVE");
        }

        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        auditService.log(id, "UPDATE_STATUS", "USER", id.toString(), "Status changed to " + status);

        return "User status updated to " + status;
    }

    @Override
    public String deleteUser(Long id) {

        User user = getUserById(id);
        userRepository.delete(user);

        auditService.log(id, "DELETE_USER", "USER", id.toString(), "User deleted");

        return "User deleted successfully";
    }

    @Override
    public List<User> getUsersByRole(String role) {

        List<User> users = userRepository.findByRole(role);

        if (users.isEmpty()) {
            throw new RuntimeException("No users found with role: " + role);
        }

        return users;
    }
}



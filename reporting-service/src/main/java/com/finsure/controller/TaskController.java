package com.finsure.controller;

import com.finsure.dto.TaskDTO;
import com.finsure.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;


    @PostMapping
    public ResponseEntity<?> createTask(
            @RequestHeader("X-User-Role") String role,
            @RequestBody TaskDTO dto) {

        if (!role.equals("LOAN_OFFICER") && !role.equals("BRANCH_MANAGER")
                && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only LOAN_OFFICER, BRANCH_MANAGER or ADMIN can create tasks");
        }
        return new ResponseEntity<>(
                taskService.createTask(dto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<?> getAllTasks(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only BRANCH_MANAGER or ADMIN can view all tasks");
        }
        return ResponseEntity.ok(taskService.getAllTasks());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {

        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(
            @RequestHeader("X-User-Role") String role,
            @PathVariable String status) {

        if (!role.equals("BRANCH_MANAGER") && !role.equals("ADMIN")
                && !role.equals("LOAN_OFFICER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role");
        }
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(taskService.getTaskById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id,
            @RequestBody TaskDTO dto) {

        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete tasks");
        }
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/internal")
    public ResponseEntity<?> createTaskInternal(
            @RequestHeader(value = "X-Internal-Call", required = false)
            String internalCall,
            @RequestBody TaskDTO dto) {

        if (!"true".equals(internalCall)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Internal endpoint only");
        }
        return ResponseEntity.ok(taskService.createTask(dto));
    }
}

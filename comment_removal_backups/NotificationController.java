package com.finsure.controller;

import com.finsure.dto.NotificationDTO;
import com.finsure.service.NotificationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationServiceImpl notificationService;


    @PostMapping
    public ResponseEntity<?> createNotification(
            @RequestHeader("X-User-Role") String role,
            @RequestBody NotificationDTO dto) {

        if (!role.equals("ADMIN") && !role.equals("LOAN_OFFICER")
                && !role.equals("BRANCH_MANAGER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Insufficient role to create notifications");
        }
        return new ResponseEntity<>(
                notificationService.createNotification(dto), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<?> getAllNotifications(
            @RequestHeader("X-User-Role") String role) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can view all notifications");
        }
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getNotificationsByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {


        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(userId));
    }


    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<?> getUnreadByUser(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                notificationService.getUnreadByUser(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(id));
    }


    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        return ResponseEntity.ok(notificationService.markAsRead(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotification(
            @RequestHeader("X-User-Role") String role,
            @PathVariable Long id) {

        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Access Denied: Only ADMIN can delete notifications");
        }
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification Deleted Successfully");
    }






 @PostMapping("/internal")
 public ResponseEntity<?> createNotificationInternal(
         @RequestHeader(value = "X-Internal-Call", required = false)
         String internalCall,
         @RequestBody NotificationDTO dto) {

     if (!"true".equals(internalCall)) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN)
                 .body("Access Denied: Internal endpoint only");
     }
     return ResponseEntity.ok(notificationService.createNotification(dto));
 }
 
}

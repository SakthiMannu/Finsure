package com.finsure.service;

import com.finsure.dto.NotificationDTO;
import com.finsure.entity.NotificationEntity;
import com.finsure.exception.ResourceNotFoundException;
import com.finsure.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public NotificationDTO createNotification(NotificationDTO dto) {
        NotificationEntity n = new NotificationEntity();
        n.setUserId(dto.getUserId());
        n.setEntityId(dto.getEntityId());
        n.setMessage(dto.getMessage());
        n.setCategory(dto.getCategory());
        n.setStatus("UNREAD");
        n.setCreatedAt(LocalDateTime.now());
        return mapToDTO(notificationRepo.save(n));
    }

    public List<NotificationDTO> getAllNotifications() {
        return notificationRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> getNotificationsByUser(Long userId) {
        List<NotificationEntity> list = notificationRepo.findByUserId(userId);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Notifications Found For User ID: " + userId);
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<NotificationDTO> getUnreadByUser(Long userId) {
        List<NotificationEntity> list =
                notificationRepo.findByUserIdAndStatus(userId, "UNREAD");
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Unread Notifications For User ID: " + userId);
        }
        return list.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Long id) {
        NotificationEntity n = notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification Not Found With ID: " + id));
        return mapToDTO(n);
    }


    public NotificationDTO markAsRead(Long id) {
        NotificationEntity n = notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification Not Found With ID: " + id));
        n.setStatus("READ");
        return mapToDTO(notificationRepo.save(n));
    }

    public void deleteNotification(Long id) {
        notificationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Notification Not Found With ID: " + id));
        notificationRepo.deleteById(id);
    }

    private NotificationDTO mapToDTO(NotificationEntity n) {
        NotificationDTO dto = new NotificationDTO();
        dto.setNotificationId(n.getNotificationId());
        dto.setUserId(n.getUserId());
        dto.setEntityId(n.getEntityId());
        dto.setMessage(n.getMessage());
        dto.setCategory(n.getCategory());
        dto.setStatus(n.getStatus());
        dto.setCreatedAt(n.getCreatedAt());
        return dto;
    }
}

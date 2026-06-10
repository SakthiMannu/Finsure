package com.finsure.service;

import com.finsure.dto.TaskDTO;
import com.finsure.entity.TaskEntity;
import com.finsure.exception.ResourceNotFoundException;
import com.finsure.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepo;

    public TaskDTO createTask(TaskDTO dto) {
        TaskEntity task = new TaskEntity();
        task.setAssignedToUserId(dto.getAssignedToUserId());
        task.setRelatedEntityId(dto.getRelatedEntityId());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus("PENDING");
        return mapToDTO(taskRepo.save(task));
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByUser(Long userId) {
        List<TaskEntity> tasks = taskRepo.findByAssignedToUserId(userId);
        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Tasks Found For User ID: " + userId);
        }
        return tasks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> getTasksByStatus(String status) {
        List<TaskEntity> tasks = taskRepo.findByStatus(status);
        if (tasks.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No Tasks Found With Status: " + status);
        }
        return tasks.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public TaskDTO getTaskById(Long id) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task Not Found With ID: " + id));
        return mapToDTO(task);
    }

    public TaskDTO updateTask(Long id, TaskDTO dto) {
        TaskEntity task = taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task Not Found With ID: " + id));

        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setStatus(dto.getStatus());
        task.setRelatedEntityId(dto.getRelatedEntityId());

        return mapToDTO(taskRepo.save(task));
    }

    public void deleteTask(Long id) {
        taskRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Task Not Found With ID: " + id));
        taskRepo.deleteById(id);
    }

    private TaskDTO mapToDTO(TaskEntity task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getTaskId());
        dto.setAssignedToUserId(task.getAssignedToUserId());
        dto.setRelatedEntityId(task.getRelatedEntityId());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setStatus(task.getStatus());
        return dto;
    }
}
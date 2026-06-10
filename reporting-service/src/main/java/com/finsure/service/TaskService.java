package com.finsure.service;

import com.finsure.dto.TaskDTO;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO dto);
    List<TaskDTO> getAllTasks();
    List<TaskDTO> getTasksByUser(Long userId);
    List<TaskDTO> getTasksByStatus(String status);
    TaskDTO getTaskById(Long id);
    TaskDTO updateTask(Long id, TaskDTO dto);
    void deleteTask(Long id);
}
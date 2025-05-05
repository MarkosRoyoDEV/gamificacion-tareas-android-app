package com.marcosroyo.backend.service;

import com.marcosroyo.backend.dto.TaskDto;
import com.marcosroyo.backend.dto.mapper.TaskDtoMapper;
import com.marcosroyo.backend.exceptions.ResourceNotFoundException;
import com.marcosroyo.backend.model.Task;
import com.marcosroyo.backend.model.User;
import com.marcosroyo.backend.repository.TaskRepository;
import com.marcosroyo.backend.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

  @Autowired
  private TaskDtoMapper taskDtoMapper;

  @Autowired
  private TaskRepository taskRepo;

  @Autowired
  private UserRepository userRepo;

  public List<TaskDto> getAllTasks(Long userId, boolean isAdmin) {

    List<Task> tasks = isAdmin ? taskRepo.findAll() : taskRepo.findByAssignedToId(userId);
    return tasks.stream().map(taskDtoMapper::toDto).collect(Collectors.toList());
  }

  public TaskDto getTaskById(Long id) {
    Task task = taskRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));
    return taskDtoMapper.toDto(task);
  }

  public List<TaskDto> getIncompleteTasks(Long userId, boolean isAdmin) {
    List<Task> tasks = isAdmin
        ? taskRepo.findByIsCompletedFalse()
        : taskRepo.findByAssignedToIdAndIsCompletedFalse(userId);
    return tasks.stream().map(taskDtoMapper::toDto).collect(Collectors.toList());
  }

  public List<TaskDto> getCompletedTasks(Long userId, boolean isAdmin) {
    List<Task> tasks = isAdmin
        ? taskRepo.findByIsCompletedTrue()
        : taskRepo.findByAssignedToIdAndIsCompletedTrue(userId);
    return tasks.stream().map(taskDtoMapper::toDto).collect(Collectors.toList());
  }

  public TaskDto createTask(Task task) {
    User user = null;
    if (task.getAssignedTo() != null && task.getAssignedTo().getId() != null) {
      user = userRepo.findById(task.getAssignedTo().getId())
          .orElseThrow(() -> new ResourceNotFoundException("Usuario asignado no encontrado"));
      task.setAssignedTo(user);
    }
    return taskDtoMapper.toDto(taskRepo.save(task));
  }

  public TaskDto completeTask(Long taskId) {
    Task task = taskRepo.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));

    if (!task.isCompleted()) {
      task.setCompleted(true);
      User assignedUser = task.getAssignedTo();
      if (assignedUser != null) {
        assignedUser.addPoints(task.getRewardPoints());
        userRepo.save(assignedUser);
      }
      task = taskRepo.save(task);
    }
    return taskDtoMapper.toDto(task);
  }

  public boolean deleteTask(Long taskId, boolean isAdmin) {
    if (isAdmin) {
      if (taskRepo.existsById(taskId)) {
        taskRepo.deleteById(taskId);
        return true;
      }
    }
    return false;
  }
}

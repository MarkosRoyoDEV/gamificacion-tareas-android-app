package com.marcosroyo.backend.controller;

import com.marcosroyo.backend.dto.TaskDto;
import com.marcosroyo.backend.model.Task;
import com.marcosroyo.backend.model.User;
import com.marcosroyo.backend.service.TaskService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

  @Autowired
  private TaskService taskService;

  @GetMapping
  public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam Long userId,
      @RequestParam boolean isAdmin) {
    return ResponseEntity.ok(taskService.getAllTasks(userId, isAdmin));
  }

  @GetMapping("/incomplete")
  public ResponseEntity<List<TaskDto>> getIncompleteTasks(@RequestParam Long userId,
      @RequestParam boolean isAdmin) {
    return ResponseEntity.ok(taskService.getIncompleteTasks(userId, isAdmin));
  }

  @GetMapping("/completed")
  public ResponseEntity<List<TaskDto>> getCompletedTasks(@RequestParam Long userId,
      @RequestParam boolean isAdmin) {
    return ResponseEntity.ok(taskService.getCompletedTasks(userId, isAdmin));
  }

  @GetMapping("/{taskId}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
    return ResponseEntity.ok(taskService.getTaskById(taskId));
  }

  @PostMapping
  public ResponseEntity<TaskDto> createTask(@RequestBody Task task,
      @RequestParam(required = false) Long assignedUserId,
      @RequestParam boolean isAdmin) {
    if (!isAdmin) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (assignedUserId != null) {
      User user = new User();
      user.setId(assignedUserId);
      task.setAssignedTo(user);
    }

    TaskDto createdTask = taskService.createTask(task);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
  }

  @PutMapping("/{taskId}/complete")
  public ResponseEntity<TaskDto> completeTask(@PathVariable Long taskId) {
    return ResponseEntity.ok(taskService.completeTask(taskId));
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<?> deleteTask(@PathVariable Long taskId, @RequestParam boolean isAdmin) {
    if (taskService.deleteTask(taskId, isAdmin)) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
  }

}

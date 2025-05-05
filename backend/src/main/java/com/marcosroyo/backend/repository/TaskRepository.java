package com.marcosroyo.backend.repository;

import com.marcosroyo.backend.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

  List<Task> findByIsCompletedFalse();

  List<Task> findByIsCompletedTrue();

  List<Task> findByAssignedToId(Long userId);

  List<Task> findByAssignedToIdAndIsCompletedFalse(Long userId);
  
  List<Task> findByAssignedToIdAndIsCompletedTrue(Long userId);

}

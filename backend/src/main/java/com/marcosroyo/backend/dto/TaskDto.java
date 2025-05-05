package com.marcosroyo.backend.dto;

public class TaskDto {

  private Long id;
  private String title;
  private String description;
  private boolean completed;
  private int rewardPoints;
  private Long assignedUserId;

  public TaskDto(Long id, String title, String description, boolean completed, int rewardPoints,
      Long assignedUserId) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
    this.rewardPoints = rewardPoints;
    this.assignedUserId = assignedUserId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public int getRewardPoints() {
    return rewardPoints;
  }

  public void setRewardPoints(int rewardPoints) {
    this.rewardPoints = rewardPoints;
  }

  public Long getAssignedUserId() {
    return assignedUserId;
  }

  public void setAssignedUserId(Long assignedUserId) {
    this.assignedUserId = assignedUserId;
  }
}

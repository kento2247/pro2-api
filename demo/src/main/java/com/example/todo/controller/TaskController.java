package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

  @Autowired private TaskRepository taskRepository;

  @GetMapping("/tasks/{userId}")
  public List<Task> getAllTasksByUserId(@PathVariable Long userId) {
    return taskRepository.findByUserId(userId);
  }

  @GetMapping("/tasks/{id}")
  public Task getTask(@PathVariable Long id) {
    return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
  }

  @PostMapping("/tasks/{userId}")
  public Task createTask(@PathVariable Long userId, @RequestBody Task task) {
    return taskRepository.createTask(userId, task);
  }

  @PutMapping("/tasks/{id}")
  public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));


    return taskRepository.save(task);
  }

  @DeleteMapping("/tasks/{id}")
  public void deleteTask(@PathVariable Long id) {
    Task task =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    taskRepository.delete(task);
  }
}

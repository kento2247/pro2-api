package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks/{userId}")
    public List<Task> getAllTasksByUserId(@PathVariable Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping("/tasks/{userId}/{groupId}/{taskSetId}")
    public Task createTask(@PathVariable Long userId, @PathVariable Long groupId, @PathVariable Long taskSetId, @RequestBody Task task) {
        return taskRepository.createTask(userId, groupId, taskSetId, task);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setPeriodicTask(taskDetails.getPeriodicTask());
        task.setTaskSet(taskDetails.getTaskSet());
        task.setTitle(taskDetails.getTitle());
        task.setBody(taskDetails.getBody());
        task.setDue_date(taskDetails.getDue_date());
        task.set_completed(taskDetails.is_completed());
        return taskRepository.save(task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}

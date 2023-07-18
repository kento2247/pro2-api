package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.security.access.vote.AuthenticatedVoter.IS_AUTHENTICATED_FULLY;

@RestController
public class TaskController {

    private final UsersRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository, UsersRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/tasks/{userId}")
    public List<Task> getAllTasksByUserId(@PathVariable Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping("/tasks/{userId}/new")
    public ResponseEntity<Task> createTask(@PathVariable Long userId,
                                           @RequestBody Task task) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @Secured(IS_AUTHENTICATED_FULLY)
    @PutMapping("/tasks/{id}")
    public Task updateTask(@AuthenticationPrincipal Users user, @PathVariable Long id, @RequestBody Task taskDetails) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setBody(taskDetails.getBody());
        task.setDue_date(taskDetails.getDue_date());
        task.set_completed(taskDetails.is_completed());
        task.setPriority(taskDetails.getPriority());
        task.set_archived_on_completion(taskDetails.is_archived_on_completion());
        return taskRepository.save(task);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskRepository.delete(task);
    }
}

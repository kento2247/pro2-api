package com.example.todo.controller;

import com.example.todo.dto.NewTaskRequest;
import com.example.todo.dto.TaskDTO;
import com.example.todo.jwtUtil.JwtUtil;
import com.example.todo.model.Task;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TaskController {

    private final UsersRepository userRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;

    public TaskController(
            TaskRepository taskRepository, UsersRepository userRepository, JwtUtil jwtUtil) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/tasks")
    public List<TaskDTO> getAllTasksByUserId(@RequestHeader(value = "Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.extractNickname(jwtToken);
        Users user = userRepository.findByEmail(email);
        List<Task> tasks = taskRepository.findByUserId(user.getId());

        List<TaskDTO> taskDTOs = tasks.stream().map(this::convertToDTO).collect(Collectors.toList());

        return taskDTOs;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setBody(task.getBody());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setDue_date(task.getDue_date());
        taskDTO.set_completed(task.is_completed());
        taskDTO.set_archived_on_completion(task.is_archived_on_completion());
        taskDTO.setCreated_at(task.getCreated_at());
        taskDTO.setUpdated_at(task.getUpdated_at());
        taskDTO.setShared_users(task.getShared_users().toArray(new Users[0]));
        return taskDTO;
    }


    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping("/tasks/new")
    public ResponseEntity<TaskDTO> createTask(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody NewTaskRequest taskRequest) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.extractNickname(jwtToken);

        Users user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        Task task = new Task();
        task.setTitle(taskRequest.getTitle());
        task.setBody(taskRequest.getBody());
        task.setPriority(taskRequest.getPriority());
        task.set_completed(taskRequest.is_completed());
        task.set_archived_on_completion(taskRequest.is_archived_on_completion());
        task.setDue_date(taskRequest.getDue_date());
        task.setShared_users(Arrays.asList(taskRequest.getShared_users()));
        task.setUser(user);

        task.setCreated_at(taskRequest.getCreated_at());
        task.setUpdated_at(taskRequest.getUpdated_at());

        Task savedTask = taskRepository.save(task);

        TaskDTO taskDTO = convertToDTO(savedTask);

        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(
            @AuthenticationPrincipal Users user, @PathVariable Long id, @RequestBody Task taskDetails) {
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

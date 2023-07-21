package com.example.todo.controller;

import com.example.todo.dto.NewTaskRequest;
import com.example.todo.dto.TaskDTO;
import com.example.todo.jwtUtil.JwtUtil;
import com.example.todo.model.Task;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        List<Task> ownedTasks = taskRepository.findTasksByUserId(user.getId());
        List<Task> sharedTasks = taskRepository.findSharedTasksByUserId(user.getId());

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(ownedTasks);
        allTasks.addAll(sharedTasks);

        // Sort the tasks by priority in descending order
        Collections.sort(allTasks, Comparator.comparingInt(Task::getPriority).reversed());
        List<TaskDTO> taskDTOs = allTasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return taskDTOs;
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setBody(task.getBody());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setDue_date(task.getDue_date());
        taskDTO.set_completed(task.isCompleted());
        taskDTO.set_archived_on_completion(task.isArchivedOnCompletion());
        taskDTO.setCreated_at(task.getCreated_at());
        taskDTO.setUpdated_at(task.getUpdated_at());

        if (task.getShared_users() != null) {
            long[] userIds = task.getShared_users().stream()
                    .mapToLong(Users::getId)
                    .toArray();
            taskDTO.setShared_users(userIds);
        } else {
            taskDTO.setShared_users(new long[]{});
        }
        return taskDTO;
    }


    @GetMapping("/tasks/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PostMapping("/tasks/new")
    public TaskDTO createTask(
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
        task.setCompleted(taskRequest.is_completed());
        task.setArchivedOnCompletion(taskRequest.is_archived_on_completion());
        task.setDue_date(taskRequest.getDue_date());
        for (long userId : taskRequest.getShared_users()) {
            Users sharedUser = userRepository.findById(userId).orElse(null);
            if (sharedUser != null) {
                task.addSharedUser(sharedUser);
            }
        }
        task.setUser(user);

        task.setCreated_at(taskRequest.getCreated_at());
        task.setUpdated_at(taskRequest.getUpdated_at());

        taskRepository.save(task);
        return convertToDTO(task);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(
            @PathVariable Long id, @RequestBody TaskDTO taskDetails) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        task.setTitle(taskDetails.getTitle());
        task.setBody(taskDetails.getBody());
        task.setDue_date(taskDetails.getDue_date());
        task.setCompleted(taskDetails.is_completed());
        task.setPriority(taskDetails.getPriority());
        task.setArchivedOnCompletion(taskDetails.is_archived_on_completion());
        for (long userId : taskDetails.getShared_users()) {
            Users sharedUser = userRepository.findById(userId).orElse(null);
            if (sharedUser != null && !task.getShared_users().contains(sharedUser)) {
                task.addSharedUser(sharedUser);
            }
        }
        task.setUpdated_at(taskDetails.getUpdated_at());
        return taskRepository.save(task);
    }
}

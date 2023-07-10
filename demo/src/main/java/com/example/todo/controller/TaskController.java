package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.model.TaskSet;
import com.example.todo.model.UserGroup;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.TaskSetRepository;
import com.example.todo.repository.UserGroupRepository;
import com.example.todo.repository.UsersRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {

    private final TaskSetRepository taskSetRepository;
    private final UserGroupRepository userGroupRepository;
    private final UsersRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository, TaskSetRepository taskSetRepository, UserGroupRepository UserGroupRepository, UsersRepository userRepository) {
        this.taskRepository = taskRepository;
        this.taskSetRepository = taskSetRepository;
        this.userGroupRepository = UserGroupRepository;
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

    @PostMapping("/tasks/{userId}/{groupId}/{taskSetId}")
    public ResponseEntity<Task> createTask(@PathVariable Long userId,
                                           @PathVariable Long groupId,
                                           @PathVariable Long taskSetId,
                                           @RequestBody Task task) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
        UserGroup group = (UserGroup) userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found with id " + groupId));
        TaskSet taskSet = taskSetRepository.findById(taskSetId)
                .orElseThrow(() -> new RuntimeException("TaskSet not found with id " + taskSetId));

        if (!group.getUsers().contains(user)) {
            throw new RuntimeException("User with id " + userId + " is not in the group with id " + groupId);
        }

        if (!group.getTaskSet().contains(taskSet)) {
            throw new RuntimeException("TaskSet with id " + taskSetId + " is not in the group with id " + groupId);
        }

        task.setTaskSet(taskSet);
        return ResponseEntity.ok(taskRepository.save(task));
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {
        Task task =
                taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
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

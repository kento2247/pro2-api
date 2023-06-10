package com.example.todo.controller;

import com.example.todo.model.TaskSet;
import com.example.todo.repository.TaskSetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskSetController {

    @Autowired
    private TaskSetRepository taskSetRepository;

    @GetMapping("/taskSet/{userId}")
    public List<TaskSet> getAllTaskSetByUserId(@PathVariable Long userId) {
        return taskSetRepository.findByUserId(userId);
    }

    @GetMapping("/taskSet/{id}")
    public TaskSet getTaskSet(@PathVariable Long id) {
        return taskSetRepository.findById(id).orElseThrow(() -> new RuntimeException("TaskSet not found"));
    }

    @PostMapping("/taskSet/{userId}/{groupId}")
    public TaskSet createTaskSet(@PathVariable Long userId, @PathVariable Long groupId, @RequestBody TaskSet taskSet) {
        return taskSetRepository.createTaskSet(userId, groupId, taskSet);
    }

    @PutMapping("/tasks/{id}")
    public TaskSet updateTask(@PathVariable Long id, @RequestBody TaskSet taskSetDetails) {
        TaskSet taskSet =
                taskSetRepository.findById(id).orElseThrow(() -> new RuntimeException("TaskSet not found"));
        taskSet.setUserGroup(taskSetDetails.getUserGroup());
        taskSet.setTasks(taskSetDetails.getTasks());
        taskSet.setTitle(taskSetDetails.getTitle());
        taskSet.setBody(taskSetDetails.getBody());
        taskSet.setDue_date(taskSetDetails.getDue_date());
        return taskSetRepository.save(taskSet);
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        TaskSet task =
                taskSetRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
        taskSetRepository.delete(task);
    }
}


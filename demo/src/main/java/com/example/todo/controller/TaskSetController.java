package com.example.todo.controller;

import com.example.todo.model.TaskSet;
import com.example.todo.repository.TaskSetRepository;
import com.example.todo.service.TaskSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskSetController {

    @Autowired
    private TaskSetRepository taskSetRepository;

    @Autowired
    private TaskSetService taskSetService;

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
        return taskSetService.createTaskSet(userId, groupId, taskSet);
    }

}


package com.example.todo.repository;
import com.example.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @GetMapping("/tasks/{userId}")
    public List<Task> findByUserId(@PathVariable Long userId);

    @PostMapping("/tasks/{userId}")
    Task createTask(@PathVariable Long userId, Task task);
}

package com.example.todo.repository;

import com.example.todo.model.TaskSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskSetRepository extends JpaRepository<TaskSet, Long> {
    TaskSet createTaskSet(Long userId, Long groupId, TaskSet taskSet);

    List<TaskSet> findByUserId(Long userId);
}

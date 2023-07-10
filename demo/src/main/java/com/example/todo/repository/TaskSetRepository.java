package com.example.todo.repository;

import com.example.todo.model.TaskSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskSetRepository extends JpaRepository<TaskSet, Long> {

    @Query("SELECT ts FROM TaskSet ts JOIN ts.userGroup ug JOIN ug.users u WHERE u.id = :userId")
    List<TaskSet> findByUserId(@Param("userId") Long userId);
}

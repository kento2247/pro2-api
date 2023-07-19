package com.example.todo.repository;

import com.example.todo.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  @Query("SELECT t FROM Task t WHERE t.user.id = :userId ")
  List<Task> findByUserId(@Param("userId") Long userId);

  Task save(Task task);
}

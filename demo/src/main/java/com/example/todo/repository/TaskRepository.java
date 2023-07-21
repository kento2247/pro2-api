package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId ")
    List<Task> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND (t.completed = false OR (t.completed = true AND t.archivedOnCompletion = true))")
    List<Task> findTasksByUserId(@Param("userId") Long userId);


    Task save(Task task);

    @Query("SELECT t FROM Task t JOIN t.shared_users u WHERE u.id = :userId AND t.completed = false AND t.archivedOnCompletion = true")
    List<Task> findSharedTasksByUserId(Long userId);
}

package com.example.todo.repository;

import com.example.todo.model.UserGroupPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupPermissionRepository extends JpaRepository<UserGroupPermission, Long> {
}

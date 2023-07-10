package com.example.todo.repository;

import com.example.todo.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByNickname(String nickname);

    Users findByEmail(String email);

}

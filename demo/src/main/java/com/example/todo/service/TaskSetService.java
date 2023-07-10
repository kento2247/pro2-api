package com.example.todo.service;

import com.example.todo.model.TaskSet;
import com.example.todo.model.UserGroup;
import com.example.todo.model.Users;
import com.example.todo.repository.TaskSetRepository;
import com.example.todo.repository.UserGroupRepository;
import com.example.todo.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskSetService {

    @Autowired
    private TaskSetRepository taskSetRepository;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    public TaskSet createTaskSet(Long userId, Long groupId, TaskSet taskSet) {
        UserGroup userGroup = (UserGroup) userGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("UserGroup not found with id " + groupId));
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        // make sure user is part of the group
        if (!userGroup.getUsers().contains(user)) {
            throw new RuntimeException("User not part of the group");
        }

        taskSet.setUserGroup(userGroup);
        return taskSetRepository.save(taskSet);
    }
}

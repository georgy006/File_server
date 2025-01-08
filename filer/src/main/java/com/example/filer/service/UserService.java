package com.example.filer.service;

import com.example.filer.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    User createUser(User user);

    User getUserByName(String userName);
    void deleteUserById(Long id);
    String getUserStatus(String username);
}

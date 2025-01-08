package com.example.filer.service.impl;

import com.example.filer.models.User;
import com.example.filer.repository.UserRepository;
import com.example.filer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByName(String userName) {
        return userRepository.findByUsername(userName);
    }
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public String getUserStatus(String username) {
        User user = userRepository.findByUsername(username);
        return (user != null) ? user.getStatus() : "Простой";
    }

}

package com.example.filer.controller;

import com.example.filer.jwt.JwtUtil;
import com.example.filer.models.User;
import com.example.filer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;
    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        User existingUser = userService.getUserByName(user.getUsername());

        if (existingUser != null && userService.verifyPassword(user.getPassword(), existingUser.getPassword())) {
            String token = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getStatus());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(401).body("Неверный логин или пароль.");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @GetMapping("/{username}")
    public User getUserByName(@PathVariable String username) {
        return userService.getUserByName(username);
    }
    @DeleteMapping("/{id}")
    public void deleteFile(@PathVariable Long id) {
        userService.deleteUserById(id);
    }
}

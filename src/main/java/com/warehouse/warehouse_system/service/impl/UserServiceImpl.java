package com.warehouse.warehouse_system.service.impl;

import com.warehouse.warehouse_system.entity.User;
import com.warehouse.warehouse_system.repository.UserRepository;
import com.warehouse.warehouse_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }
    
    @Override
    public User createUser(User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("temp123");
        }
        user.setPassword(user.getPassword());
        return userRepository.save(user);
    }
    
    @Override
    public User updateUser(int id, User user) {
        Optional<User> existing = userRepository.findById(id);
        if (existing.isPresent()) {
            User existingUser = existing.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setRole(user.getRole());
            return userRepository.save(existingUser);
        }
        return null;
    }
    
    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
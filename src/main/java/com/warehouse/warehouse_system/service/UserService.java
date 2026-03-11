package com.warehouse.warehouse_system.service;

import com.warehouse.warehouse_system.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();
    Optional<User> getUserById(int id);
    User createUser(User user);
    User updateUser(int id, User user);
    void deleteUser(int id);
}
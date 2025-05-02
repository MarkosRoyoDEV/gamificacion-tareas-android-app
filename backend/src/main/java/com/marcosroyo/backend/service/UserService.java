package com.marcosroyo.backend.service;

import com.marcosroyo.backend.model.User;
import com.marcosroyo.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepo.findById(id);
    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User updateUser(Long id, User userActualizado) {
        return userRepo.findById(id)
                .map(user -> {
                    user.setUsername(userActualizado.getUsername());
                    user.setAdmin(userActualizado.isAdmin());
                    return userRepo.save(user);
                }).orElse(null);
    }

    public boolean deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }
}

package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(String name) {
        UserEntity user = new UserEntity();
        user.name = name;
        return userRepository.save(user);
    }

    public UserEntity getUser(int id) {
        return userRepository.getById(id);
    }

    public ArrayList<UserEntity> getAllUsers() {
        return userRepository.getAll();
    }

    public UserEntity updateUser(int id, String newName) {
        UserEntity user = userRepository.getById(id);
        if (user != null) {
            user.name = newName;
            return userRepository.update(user);
        }
        return null;
    }

    public void deleteUser(int id) {
        userRepository.delete(id);
    }

}

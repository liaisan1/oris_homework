package com;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public class UserRepository {
    ArrayList<UserEntity> users = new ArrayList<>();
    int currentId = 1;

    public UserEntity save(UserEntity user) {
        user.id = currentId++;
        users.add(user);
        return user;
    }

    public UserEntity getById(int id) {
        for (UserEntity user : users) {
            if (user.id == id) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<UserEntity> getAll() {
        return users;
    }

    public UserEntity update(UserEntity user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id == user.id) {
                users.set(i, user);
                return user;
            }
        }
        return null;
    }

    public void delete(int id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).id == id) {
                users.remove(i);
                return;
            }
        }
    }
}

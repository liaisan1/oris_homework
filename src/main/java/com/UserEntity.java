package com;

public class UserEntity {
    int id;
    String name;

    public UserEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserEntity() {}

    public String toString() {
        return "id: " + id + " name: " + name;
    }
}

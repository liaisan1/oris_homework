package com;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);

        UserService userService = context.getBean(UserService.class);

        UserEntity user1 = userService.createUser("Катя");
        UserEntity user2 = userService.createUser("Даша");
        System.out.println(user1);
        System.out.println(user2);

        System.out.println(userService.getUser(1));

        userService.updateUser(1, "Катя уже взрослая");
        System.out.println(userService.getUser(1));

        userService.deleteUser(1);

        userService.getAllUsers().forEach(System.out::println);
    }
}

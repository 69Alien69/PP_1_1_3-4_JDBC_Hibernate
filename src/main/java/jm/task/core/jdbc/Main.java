package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = new ArrayList<>();

        userList.add(new User("Олег", "Милько", (byte) 12));
        userList.add(new User("Мария", "Королькова", (byte) 37));
        userList.add(new User("Дмитрий", "Козлов", (byte) 119));
        userList.add(new User("Лев", "Толстой", (byte) 2));

        userService.createUsersTable();
        for(User user : userList) {
            userService.saveUser(user.getName(), user.getLastName(), user.getAge());
            System.out.println("User с именем " + user.getName() + " добавлен в базу данных");
        }
        List<User> userListFromDB = userService.getAllUsers();
        for(User user : userListFromDB) {
            System.out.println(user.toString());
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();

    }
}

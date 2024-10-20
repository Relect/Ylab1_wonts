package website.ylab.service;

import lombok.Getter;
import website.ylab.custom.Admin;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import java.util.*;

public class DataUsers {

    @Getter
    final HashMap<String, User> users = new HashMap<>();
    private final HashSet<String> passwords = new HashSet<>();

    {
        User admin = new User(Admin.name, Admin.email, Admin.password);
        admin.setAdmin(true);
        users.put(Admin.email, admin);
        passwords.add(Admin.password);
    }

    public void addUser(Read in, Write out) {

        out.writeLn("Введите имя пользователя");
        String name = in.readLn();

        out.writeLn("Введите email пользователя");
        String email = in.readLn();
        while (users.containsKey(email)) {
            out.writeLn("Этот email занят, введите другой");
            email = in.readLn();
        }

        out.writeLn("Введите пароль пользователя");
        String password = in.readLn();
        while (passwords.contains(password)) {
            out.writeLn("Этот пароль занят, введите другой");
            email = in.readLn();
        }

        User user = new User(name, email, password);
        passwords.add(password);
        users.put(email, user);
    }

    public User login(Read in, Write out) {

        out.writeLn("Введите email пользователя");
        String email = in.readLn();
        while (!users.containsKey(email)) {
            out.writeLn("такой email отсутствует, введите другой или exit");
            email = in.readLn();
            if (email.equals("exit")) return null;
        }
        out.writeLn("Введите пароль пользователя");
        String password = in.readLn();
        while (!users.get(email).getPassword().equals(password)) {
            out.writeLn("Пароль не совпадает, введите правильный пароль или exit");
            password = in.readLn();
            if (password.equals("exit")) return null;
        }
        out.writeLn("вход выполнен");
        User user = users.get(email);
        out.writeLn(user.toString());
        return user;
    }

    public void deleteUser(String email) {
        String pass = users.get(email).getPassword();
        passwords.remove(pass);
        users.remove(email);
    }

    public void editUser(String email, Read in, Write out) {
        while (true) {
            out.writeLn("""                    
                    Для редактирования пользователя введите: 
                    1 - имя пользователя,
                    2 - email пользователя, 
                    3 - пароль пользователя.
                    exit для выхода в предыдущее меню введите.""");
            String command = in.readLn();

            switch (command) {
                case "1":
                    out.writeLn("Введите новое имя пользователя");
                    String name = in.readLn();
                    User user = users.get(email);
                    user.setName(name);
                    users.put(email, user);
                    break;
                case "2":
                    User user1 = users.get(email);
                    out.writeLn("Введите новый email пользователя");
                    String newEmail = in.readLn();
                    while (users.containsKey(newEmail)) {
                        out.writeLn("Этот email занят, введите другой");
                        email = in.readLn();
                    }
                    users.remove(email);
                    users.put(newEmail, user1);
                    break;
                case "3":
                    User user2 = users.get(email);
                    String password = user2.getPassword();
                    out.writeLn("Введите новый пароль пользователя");
                    String newPassword = in.readLn();
                    while (passwords.contains(newPassword)) {
                        out.writeLn("Этот пароль занят, введите другой");
                        email = in.readLn();
                    }
                    passwords.remove(password);
                    passwords.add(newPassword);
                    user2.setPassword(newPassword);
                    users.put(email, user2);
                case "exit":
                    return;
                default:
                    out.writeLn("команда неверна, повторите заново");
            }
        }
    }
}

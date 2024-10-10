package website.ylab;

import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import java.util.*;

public class Data {

    final HashMap<String, User> users = new HashMap<>();
    final HashSet<String> passwords = new HashSet<>();

    private User userLogin = null;

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
}

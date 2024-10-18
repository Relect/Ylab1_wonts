package website.ylab.service;

import lombok.Getter;
import website.ylab.custom.Admin;
import website.ylab.db.DBManager;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;

import java.sql.*;
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
        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT count(*) from new.users where email = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            while (count != 0) {
                out.writeLn("Этот email занят, введите другой");
                email = in.readLn();
                ps.setString(1, email);
                rs = ps.executeQuery();
                rs.next();
                count = rs.getInt("count");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.writeLn("Введите пароль пользователя");
        String password = in.readLn();

        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT count(*) from new.users where password = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ResultSet rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            while (count != 0) {
                rs.close();
                ps.close();
                out.writeLn("Этот пароль занят, введите другой");

                ps = conn.prepareStatement(sql);
                password = in.readLn();
                ps.setString(1, password);
                rs = ps.executeQuery();
                rs.next();
                count = rs.getInt("count");

            }
            rs.close();
            ps.close();

            sql = "INSERT INTO new.users (name, email, password) " +
                    " VALUES (?, ?, ?);";
            ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            int insert = ps.executeUpdate();
            if (insert == 1) {
                out.writeLn("Пользователь успешно добавлен.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        User user = new User(name, email, password);
        passwords.add(password);
        users.put(email, user);
    }

    public User login(Read in, Write out) {

        out.writeLn("Введите email пользователя");
        String email = in.readLn();
        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT id, name, email, password, admin, block from new.users where email = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (!rs.next()) {
                out.writeLn("Этот email занят, введите другой");
                email = in.readLn();
                rs.close();
                ps.close();
                ps = conn.prepareStatement(sql);
                ps.setString(1, email);
                rs = ps.executeQuery();
            }

            out.writeLn("Введите пароль пользователя");
            String password = in.readLn();

            while (!rs.getString("password").equals(password)) {
                out.writeLn("Пароль не совпадает, введите правильный пароль или exit");
                password = in.readLn();
                if (password.equals("exit")) return null;
            }
            String name = rs.getString("name");
            User user = new User(name, email, password);
            user.setId(rs.getLong("id"));
            user.setAdmin(rs.getBoolean("admin"));
            user.setBlock(rs.getBoolean("block"));
            out.writeLn("вход выполнен");
            return user;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(String email) {

        try (Connection conn = DBManager.getConn()) {
            String sql = "SELECT id from new.users where email = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long user_id = rs.getLong("id");
                String sql2 = "SELECT id from new.wonts where user_id = ?;";
                PreparedStatement ps2 = conn.prepareStatement(sql2);
                ps2.setLong(1, user_id);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    long wont_id = rs2.getLong("id")
                    String sql3 = "DELETE from new.done where wont_id = ?;";
                    PreparedStatement ps3 = conn.prepareStatement(sql3);
                    ps3.setLong(1, wont_id);
                    ps3.executeUpdate();
                    String slq2_del = "DELETE from new.wonts where id = ?;";
                    PreparedStatement ps4 = conn.prepareStatement(slq2_del);
                    ps4.setLong(1, wont_id);
                    ps4.executeUpdate();
                }
                String slq1_del = "DELETE from new.users where id = ?;";
                PreparedStatement ps5 = conn.prepareStatement(slq1_del);
                ps5.setLong(1, user_id);
                ps5.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

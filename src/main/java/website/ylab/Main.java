package website.ylab;


import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import website.ylab.db.DBManager;
import website.ylab.in.Read;
import website.ylab.model.User;
import website.ylab.out.Write;
import website.ylab.service.DataUsers;
import website.ylab.service.DataWonts;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        Read in = new Read();
        Write out = new Write();

        try (Connection conn = DBManager.getConn()) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            database.setDefaultSchemaName("new");
            Liquibase liquibase =
                    new Liquibase("db/changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            out.writeLn("migrate successful");
        } catch (LiquibaseException e) {
            out.writeLn("ошибка liquibase");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        out.writeLn("");

        DataUsers dataUsers = new DataUsers();
        DataWonts dataWonts = new DataWonts(dataUsers);
        String command;

        while (true) {
            out.writeLn("""
                    1 Для регистрации нового пользователя введите,
                    2 для авторизации введите,
                    exit для выхода введите.""");
            command = in.readLn();

            switch (command) {
                case "exit":
                    out.writeLn("Thank you for choosing Ylab");
                    System.exit(0);
                case "1":
                    dataUsers.addUser(in, out);
                    break;
                case "2":
                    User user = dataUsers.login(in, out);
                    if (user != null) {
                        if (user.isAdmin()) {
                            while (true) {
                                boolean exit = false;
                                out.writeLn("""
                                        1 для блокировки пользователя,
                                        2 для разблокировки пользователя,
                                        3 для удаления пользователя,
                                        exit для выхода в предыдущее меню""");
                                String commandAdmin = in.readLn();

                                switch (commandAdmin) {
                                    case "1":
                                        out.writeLn("введите email пользователя");
                                        String email = in.readLn();

                                        try (Connection conn = DBManager.getConn()) {
                                            String sql = "UPDATE new.users set block = true WHERE email = ?;";
                                            PreparedStatement ps = conn.prepareStatement(sql);
                                            ps.setString(1, email);
                                            int count = ps.executeUpdate();
                                            if (count != 0) {
                                                out.writeLn("пользователь " + email + " заблокирован.");
                                            } else {
                                                out.writeLn("пользователь " + email + " не найден");
                                            }
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        break;
                                    case "2":
                                        out.writeLn("введите email пользователя");
                                        String email1 = in.readLn();

                                        try (Connection conn = DBManager.getConn()) {
                                            String sql = "UPDATE new.users set block = false WHERE email = ?;";
                                            PreparedStatement ps = conn.prepareStatement(sql);
                                            ps.setString(1, email1);
                                            int count = ps.executeUpdate();
                                            if (count != 0) {
                                                out.writeLn("пользователь " + email1 + " разблокирован.");
                                            } else {
                                                out.writeLn("пользователь " + email1 + " не найден");
                                            }
                                        } catch (SQLException e) {
                                            throw new RuntimeException(e);
                                        }
                                        break;
                                    case "3":
                                        out.writeLn("введите email пользователя");
                                        String email2 = in.readLn();
                                        if (dataUsers.deleteUser(email2, out)) {
                                            out.writeLn("пользователь " + email2 + " удалён.");
                                        } else {
                                            out.writeLn("пользователь " + email2 + " не найден");
                                        }
                                        break;
                                    case "exit":
                                        exit = true;
                                        break;
                                    default:
                                        out.writeLn("Команда неверна, повторите заново.");
                                }
                                if (exit) break;
                            }
                        } else {
                            if (user.isBlock()) {
                                out.writeLn("пользователь " + user.getEmail() + " заблокирован");
                            } else {
                                while (true) {
                                    boolean exit = false;
                                    out.writeLn("""
                                            1 введите для редактирования пользователя,
                                            2 введите для удаления пользователя,
                                            3 введите для управления привычками,
                                            exit введите для выхода в предыдущее меню.""");
                                    String command2 = in.readLn();

                                    switch (command2) {
                                        case "1":
                                            dataUsers.editUser(user.getEmail(), in, out);
                                            break;
                                        case "2":
                                            if (dataUsers.deleteUser(user.getEmail(), out)) {
                                                out.writeLn("пользователь " + user.getEmail() + " удалён.");
                                            } else {
                                                out.writeLn("пользователь " + user.getEmail() + " не найден");
                                            }
                                            exit = true;
                                            break;
                                        case "3":
                                            dataWonts.controlWonts(in, out, user);
                                            break;
                                        case "exit":
                                            exit = true;
                                            break;
                                        default:
                                            out.writeLn("Команда неверна, повторите заново.");
                                    }
                                    if (exit) break;
                                }
                            }
                        }
                    }
                    break;
                default:
                    out.writeLn("Команда неверна, повторите заново.");
            }
        }

    }
}
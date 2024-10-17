package website.ylab.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Manager {
    private static String USER = "ylabUser";
    private static String PASSWORD = "123";
    private static String DATABASE = "db_ylab";
    private static String URL = "jdbc:postgresql://localhost:5433/" + DATABASE;

    public static Connection getConn() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("ошибка регистрации драйвера");
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("ошибка соединения");
        }

        return connection;
    }

    public static Connection migrate(Connection conn) {
        return conn;
    }

}

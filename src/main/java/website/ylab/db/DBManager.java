package website.ylab.db;

import lombok.Getter;
import lombok.Setter;
import website.ylab.out.Write;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {
    public static final Properties properties = new Properties();
    public static String DATABASE_URL;
    static {
        try {
            properties.load(new FileReader("database.properties"));
            String driverName = (String) properties.get("db.driver");
            Class.forName(driverName);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        DATABASE_URL = (String) properties.get("db.url");
    }
    private DBManager() {}
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, properties);
    }
}
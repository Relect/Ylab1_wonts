package website.ylab.db;

import website.ylab.out.Write;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBManager {
    private static final Properties properties = new Properties();
    private static final String DATABASE_URL;
    static {
        try {
            properties.load(new FileReader("database.properties"));
            String driverName = (String) properties.get("db.driver");
            Class.forName(driverName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        DATABASE_URL = (String) properties.get("db.url");
    }
    private DBManager() {}
    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, properties);
    }
}
